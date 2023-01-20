package net.lizistired.herobrine.entities;

import net.lizistired.herobrine.ExampleMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static net.lizistired.herobrine.ExampleMod.HEROBRINE;
import static net.minecraft.sound.SoundEvents.AMBIENT_CAVE;


public class Herobrine extends HostileEntity implements Angerable {

    private static final TrackedData<Boolean> ANGRY = DataTracker.registerData(Herobrine.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> NO_GRAVITY = DataTracker.registerData(Herobrine.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> NO_DRAG = DataTracker.registerData(Herobrine.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int angerTime;
    private int lastAngrySoundAge = Integer.MIN_VALUE;
    private int ageWhenTargetSet;
    @Nullable
    private UUID angryAt;

    public Herobrine(EntityType<? extends HostileEntity> entityType, World world) {
        super((EntityType<? extends HostileEntity>)entityType, world);
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 500.0f, 1.0f));
        //this.goalSelector.add(1, new EatGrassGoal(this));
        this.targetSelector.add(1, new TeleportTowardsPlayerGoal(this, this::shouldAngerAt));
        //this.goalSelector.add(8, new WanderAroundGoal(this, 0.23f));
        //this.goalSelector.add(8, new LookAroundGoal(this));
        this.initCustomGoals();
    }
    protected void initCustomGoals() {
        //this.goalSelector.add(2, new MeleeAttackGoal(this, 4f, true));
        //this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));
    }
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target == null) {
            this.ageWhenTargetSet = 0;
            this.dataTracker.set(ANGRY, false);
        } else {
            this.ageWhenTargetSet = this.age;
            this.dataTracker.set(ANGRY, true);
        }
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    public int getAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void chooseRandomAngerTime() {

    }

    @Override
    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    public void playAngrySound() {
        if (this.age >= this.lastAngrySoundAge + 4) {
            this.lastAngrySoundAge = this.age;
            if (!this.isSilent()) {
                this.world.playSound(this.getX(), this.getEyeY(), this.getZ(), new BiomeMoodSound(SoundEvents.AMBIENT_CAVE, 0, 8, 2.0).getSound().value(), SoundCategory.HOSTILE, 2.5f, 1.0f, false);
            }
        }
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANGRY.equals(data) && this.world.isClient) {
            this.playAngrySound();
        }
        super.onTrackedDataSet(data);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.world, nbt);
    }

    public boolean isAngry() {
        return this.dataTracker.get(ANGRY);
    }
    @Override
    public void tick(){
        super.tick();
        world = this.getEntityWorld();
        /*if (isPlayerStaring((PlayerEntity)Objects.requireNonNull(this.getTarget()))){
            this.teleport(world.getClosestPlayer(this, 10).getX(), world.getClosestPlayer(this, 10).getY(), world.getClosestPlayer(this, 10).getZ());
        }*/
        // if distance between this entity and the player is less than 10 blocks, then teleport to the player
        /*if (world.getClosestPlayer(this, 10) != null) {
            this.teleport(world.getClosestPlayer(this, 10).getX(), world.getClosestPlayer(this, 10).getY(), world.getClosestPlayer(this, 10).getZ());
        }*/

    }

    boolean isPlayerStaring(PlayerEntity player) {
        Vec3d vec3d = player.getRotationVec(1.0f).normalize();
        Vec3d vec3d2 = new Vec3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
        double d = vec3d2.length();
        double e = vec3d.dotProduct(vec3d2 = vec3d2.normalize());
        if (e > 1.0 - 0.025 / d) {
            return player.canSee(this);
        }
        return false;
    }

    protected boolean teleportRandomly() {
        if (this.world.isClient() || !this.isAlive()) {
            return false;
        }
        double d = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
        double e = this.getY() + (double)(this.random.nextInt(64) - 32);
        double f = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, this.world); // Create the lightning bolt
        lightning.setPosition(this.getPos()); // Set its position. This will make the lightning bolt strike the player (probably not what you want)
        world.spawnEntity(lightning); // Spawn the lightning entity
        return this.teleportTo(d, e, f);
    }
    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > this.world.getBottomY() && !this.world.getBlockState(mutable).getMaterial().blocksMovement()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.world.getBlockState(mutable);
        boolean bl = blockState.getMaterial().blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (!bl || bl2) {
            return false;
        }
        Vec3d vec3d = this.getPos();
        boolean bl3 = this.teleport(x, y, z, false);
        if (bl3) {
            this.world.emitGameEvent(GameEvent.HIT_GROUND, vec3d, GameEvent.Emitter.of(this));
            if (!this.isSilent()) {
                this.world.playSound(null, this.prevX, this.prevY, this.prevZ, SoundEvents.BLOCK_STONE_STEP, this.getSoundCategory(), 1.0f, 1.0f);
                this.playSound(SoundEvents.BLOCK_STONE_STEP, 1.0f, 1.0f);
            }
        }
        return bl3;
    }

    boolean teleportTo(Entity entity) {
        Vec3d vec3d = new Vec3d(this.getX() - entity.getX(), this.getBodyY(0.5) - entity.getEyeY(), this.getZ() - entity.getZ());
        vec3d = vec3d.normalize();
        double d = 16.0;
        double e = this.getX() + (this.random.nextDouble() - 0.5) * 8.0 - vec3d.x * 16.0;
        double f = this.getY() + (double)(this.random.nextInt(16) - 8) - vec3d.y * 16.0;
        double g = this.getZ() + (this.random.nextDouble() - 0.5) * 8.0 - vec3d.z * 16.0;
        return this.teleportTo(e, f, g);
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ANGRY, false);
        this.dataTracker.startTracking(NO_GRAVITY, false);
        this.dataTracker.startTracking(NO_DRAG, false);
    }


    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!source.isOutOfWorld()) {
           if (source.name == "player") {
                //source.getSource().addVelocity(500,5,0);
                //source.getSource().startRiding(this, true);
            }
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    public boolean shouldRenderName() {
        return true;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        setNoGravity(true);
        //setNoDrag(true);
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world.toServerWorld()); // Create the lightning bolt
        lightning.setPosition(this.getPos()); // Set its position. This will make the lightning bolt strike the player (probably not what you want)
        world.spawnEntity(lightning); // Spawn the lightning entity
        ExampleMod.LOGGER.info("Herobrine has spawned!");
        this.setCustomName(Text.translatable("entity.minecraft.herobrinenametag"));

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    class TeleportTowardsPlayerGoal
            extends ActiveTargetGoal<PlayerEntity> {
        private final Herobrine herobrine;
        @Nullable
        private PlayerEntity targetPlayer;
        private int lookAtPlayerWarmup;
        private int ticksSinceUnseenTeleport;
        private final TargetPredicate staringPlayerPredicate;
        private final TargetPredicate validTargetPredicate = TargetPredicate.createAttackable().ignoreVisibility();
        private final Predicate<LivingEntity> angerPredicate;

        public TeleportTowardsPlayerGoal(Herobrine herobrine, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(herobrine, PlayerEntity.class, 10, false, false, targetPredicate);
            this.herobrine = herobrine;
            this.angerPredicate = playerEntity -> isPlayerStaring((PlayerEntity)playerEntity) || shouldAngerAt((LivingEntity)playerEntity);
            this.staringPlayerPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange()).setPredicate(this.angerPredicate);
        }

        @Override
        public boolean canStart() {
            this.targetPlayer = this.herobrine.world.getClosestPlayer(this.staringPlayerPredicate, this.herobrine);
            return this.targetPlayer != null;
        }

        @Override
        public void start() {
            this.lookAtPlayerWarmup = this.getTickCount(5);
            this.ticksSinceUnseenTeleport = 0;
        }

        @Override
        public void stop() {
            this.targetPlayer = null;
            super.stop();
        }

        @Override
        public boolean shouldContinue() {
            if (this.targetPlayer != null) {
                if (!this.angerPredicate.test(this.targetPlayer)) {
                    return false;
                }
                this.herobrine.lookAtEntity(this.targetPlayer, 10.0f, 10.0f);
                return true;
            }
            if (this.targetEntity != null && this.validTargetPredicate.test(this.herobrine, this.targetEntity)) {
                return true;
            }
            return super.shouldContinue();
        }

        @Override
        public void tick() {
            if (this.herobrine.getTarget() == null) {
                super.setTargetEntity(null);
            }
            if (this.targetPlayer != null) {
                if (--this.lookAtPlayerWarmup <= 0) {
                    this.targetEntity = this.targetPlayer;
                    this.targetPlayer = null;
                    super.start();
                }
            } else {
                if (this.targetEntity != null && !this.herobrine.hasVehicle()) {
                    if (this.herobrine.isPlayerStaring((PlayerEntity)this.targetEntity)) {
                        if (this.targetEntity.squaredDistanceTo(this.herobrine) > 16.0) {
                                this.herobrine.world.playSound(this.herobrine.getX(), this.herobrine.getEyeY(), this.herobrine.getZ(), new BiomeMoodSound(SoundEvents.AMBIENT_CAVE, 0, 8, 2.0).getSound().value(), SoundCategory.HOSTILE, 2.5f, 1.0f, false);
                            this.herobrine.teleportRandomly();
                        }
                        this.ticksSinceUnseenTeleport = 0;
                    } else if (this.targetEntity.squaredDistanceTo(this.herobrine) > 256.0 && this.ticksSinceUnseenTeleport++ >= this.getTickCount(30) && this.herobrine.teleportTo(this.targetEntity)) {
                        this.herobrine.world.playSound(this.herobrine.getX(), this.herobrine.getEyeY(), this.herobrine.getZ(), new BiomeMoodSound(SoundEvents.AMBIENT_CAVE, 0, 8, 2.0).getSound().value(), SoundCategory.HOSTILE, 2.5f, 1.0f, false);
                        this.ticksSinceUnseenTeleport = 0;
                    }
                }
                super.tick();
            }
        }
    }

    public static DefaultAttributeContainer.Builder createHerobrineAttributes() {
        return createLivingAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 2f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_ARMOR, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, Float.MAX_VALUE);
    }
}
