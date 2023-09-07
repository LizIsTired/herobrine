package net.lizistired.herobrinereturns.entities;

import com.ibm.icu.impl.SortedSetRelation;
import net.lizistired.herobrinereturns.HerobrineReturns;
import net.lizistired.herobrinereturns.utils.misc.RapidTitle;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

import static net.lizistired.herobrinereturns.HerobrineReturns.minecraftServer;


public abstract class BaseHerobrineEntity extends HostileEntity implements Angerable {

    private static final TrackedData<Boolean> ANGRY = DataTracker.registerData(BaseHerobrineEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> NO_GRAVITY = DataTracker.registerData(BaseHerobrineEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> NO_DRAG = DataTracker.registerData(BaseHerobrineEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int angerTime;
    private int lastAngrySoundAge = Integer.MIN_VALUE;
    private int ageWhenTargetSet;
    @Nullable
    private UUID angryAt;

    public BaseHerobrineEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super((EntityType<? extends HostileEntity>)entityType, world);
    }
    @Override
    protected void initGoals() {
        //this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 500.0f, 1.0f));
        //this.goalSelector.add(1, new EatGrassGoal(this));
        //this.targetSelector.add(1, new TeleportTowardsPlayerGoal(this, this::shouldAngerAt));
        //this.goalSelector.add(1, new FlyGoal(this, 500));
        //this.goalSelector.add(8, new WanderAroundGoal(this, 0.23f));
        //this.goalSelector.add(8, new LookAroundGoal(this));
        this.initCustomGoals();
    }
    protected void initCustomGoals() {
        this.goalSelector.add(2, new MeleeAttackGoal(this, 0.4f, false));
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));
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

    @Override
    protected SoundEvent getAmbientSound() {
        return new BiomeMoodSound(SoundEvents.AMBIENT_CAVE, 0, 8, 20.0).getSound().value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLOCK_ANCIENT_DEBRIS_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMITE_DEATH;
    }

    public boolean isAngry() {
        return this.dataTracker.get(ANGRY);
    }

    @Override
    public boolean cannotDespawn() {
        super.cannotDespawn();
        return true;
    }
    /*@Override
    public void tick(){
        super.tick();
        world = this.getEntityWorld();
        /*if (isPlayerStaring((PlayerEntity)Objects.requireNonNull(this.getTarget()))){
            this.teleport(world.getClosestPlayer(this, 10).getX(), world.getClosestPlayer(this, 10).getY(), world.getClosestPlayer(this, 10).getZ());
        }
        // if distance between this entity and the player is less than 10 blocks, then teleport to the player
        /*if (world.getClosestPlayer(this, 10) != null) {
            this.teleport(world.getClosestPlayer(this, 10).getX(), world.getClosestPlayer(this, 10).getY(), world.getClosestPlayer(this, 10).getZ());
        }

    }*/
    @Override
    public void tickMovement() {
        this.jumping = false;
        if (!this.world.isClient) {
            this.tickAngerLogic((ServerWorld)this.world, true);
        }
        super.tickMovement();
    }
    @Override
    protected void mobTick() {
        super.mobTick();
        if(this.getTarget() instanceof PlayerEntity){
            //if ((this.age + this.getId()) % 1200 == 0) {
            if(isPlayerStaring((PlayerEntity)this.getTarget()) && this.age > 15) {

                StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.SLOWNESS, 50, 0);
                StatusEffectInstance statusEffectInstance1 = new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 256);
                StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) this.world, this, this.getPos(), 50.0, statusEffectInstance, 50);
                List<ServerPlayerEntity> list = StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) this.world, this, this.getPos(), 50.0, statusEffectInstance1, 5);
                list.forEach(serverPlayerEntity -> {
                    if(isPlayerStaring((PlayerEntity)this.getTarget())){
                        serverPlayerEntity.networkHandler.sendPacket(new GameStateChangeS2CPacket(HerobrineReturns.HEROBRINE_APPEARANCE_EFFECT, this.isSilent() ? GameStateChangeS2CPacket.DEMO_OPEN_SCREEN : (int) 1.0f));
                        serverPlayerEntity.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0f, 1.0f);
                        try {
                            RapidTitle.function(serverPlayerEntity,"entity.minecraft.herobrine.chat", 1, 2, 1, 100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                this.age = 0;
           //}
        }
        }
        if (!this.hasPositionTarget()) {
            this.setPositionTarget(this.getBlockPos(), 16);
        }
    }

    public boolean isPlayerStaring(PlayerEntity player) {
        Vec3d vec3d = player.getRotationVec(1.0f).normalize();
        Vec3d vec3d2 = new Vec3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
        double d = vec3d2.length();
        double e = vec3d.dotProduct(vec3d2 = vec3d2.normalize());
        if (e > 1.0 - 0.025 / d) {
            return player.canSee(this);
        }
        return false;
    }

    private boolean teleportRandomly() {
        if (this.world.isClient() || !this.isAlive()) {
            return false;
        }
        double d = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
        double e = this.getY() + (double)(this.random.nextInt(64) - 32);
        double f = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, this.world); // Create the lightning bolt
        lightning.setPosition(d,e,f); // Set its position. This will make the lightning bolt strike the player (probably not what you want)
        world.spawnEntity(lightning); // Spawn the lightning entity
        return this.teleportTo(d, e, f);
    }
    protected boolean teleportTo(double x, double y, double z) {
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
    public boolean shouldRenderName() {
        return true;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        //setNoGravity(true);
        //setNoDrag(true);
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world.toServerWorld()); // Create the lightning bolt
        lightning.setPosition(this.getPos()); // Set its position. This will make the lightning bolt strike the player (probably not what you want)
        world.spawnEntity(lightning); // Spawn the lightning entity
        HerobrineReturns.LOGGER.info("Herobrine has spawned!");
        MinecraftClient.getInstance().player.sendMessage(Text.translatable("entity.minecraft.regnametag"), false);
        if (!world.isClient()) {
             minecraftServer.sendMessage(Text.translatable("entity.minecraft.regnametag"));
        } else {
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("entity.minecraft.regnametag"), false);
        }
        this.setCustomName(Text.translatable("entity.minecraft.herobrinenametag"));
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }
    static class ChasePlayerGoal
            extends Goal {
        private final BaseHerobrineEntity baseHerobrineEntity;
        @Nullable
        private LivingEntity target;

        public ChasePlayerGoal(BaseHerobrineEntity baseHerobrineEntity) {
            this.baseHerobrineEntity = baseHerobrineEntity;
            this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            this.target = this.baseHerobrineEntity.getTarget();
            if (!(this.target instanceof PlayerEntity)) {
                return false;
            }
            double d = this.target.squaredDistanceTo(this.baseHerobrineEntity);
            if (d > 256.0) {
                return false;
            }
            return this.baseHerobrineEntity.isPlayerStaring((PlayerEntity) this.target);
        }

        @Override
        public void start() {
            this.baseHerobrineEntity.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.baseHerobrineEntity.getLookControl().lookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
        }
    }
    public class TeleportTowardsPlayerGoal
            extends ActiveTargetGoal<PlayerEntity> {
        private final BaseHerobrineEntity baseHerobrineEntity;
        @Nullable
        private PlayerEntity targetPlayer;
        private int lookAtPlayerWarmup;
        private int ticksSinceUnseenTeleport;
        private final TargetPredicate staringPlayerPredicate;
        private final TargetPredicate validTargetPredicate = TargetPredicate.createAttackable().ignoreVisibility();
        private final Predicate<LivingEntity> angerPredicate;

        public TeleportTowardsPlayerGoal(BaseHerobrineEntity baseHerobrineEntity, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(baseHerobrineEntity, PlayerEntity.class, 10, false, false, targetPredicate);
            this.baseHerobrineEntity = baseHerobrineEntity;
            this.angerPredicate = playerEntity -> isPlayerStaring((PlayerEntity) playerEntity) || shouldAngerAt((LivingEntity) playerEntity);
            this.staringPlayerPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange()).setPredicate(this.angerPredicate);
        }

        @Override
        public boolean canStart() {
            this.targetPlayer = this.baseHerobrineEntity.world.getClosestPlayer(this.staringPlayerPredicate, this.baseHerobrineEntity);
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
                this.baseHerobrineEntity.lookAtEntity(this.targetPlayer, 10.0f, 10.0f);
                return true;
            }
            if (this.targetEntity != null && this.validTargetPredicate.test(this.baseHerobrineEntity, this.targetEntity)) {
                return true;
            }
            return super.shouldContinue();
        }

        @Override
        public void tick() {
            if (this.baseHerobrineEntity.getTarget() == null) {
                super.setTargetEntity(null);
            }
            if (this.targetPlayer != null) {
                if (--this.lookAtPlayerWarmup <= 0) {
                    this.targetEntity = this.targetPlayer;
                    this.targetPlayer = null;
                    super.start();
                }
            } else {
                if (this.targetEntity != null && !this.baseHerobrineEntity.hasVehicle()) {
                    if (this.baseHerobrineEntity.isPlayerStaring((PlayerEntity) this.targetEntity)) {
                        if (this.targetEntity.squaredDistanceTo(this.baseHerobrineEntity) > 16.0) {
                            HerobrineReturns.LOGGER.info("Herobrine has teleported!");
                            this.baseHerobrineEntity.world.playSound(this.baseHerobrineEntity.getX(), this.baseHerobrineEntity.getEyeY(), this.baseHerobrineEntity.getZ(), new BiomeMoodSound(SoundEvents.AMBIENT_CAVE, 0, 8, 2.0).getSound().value(), SoundCategory.HOSTILE, 2.5f, 1.0f, false);
                            this.baseHerobrineEntity.teleportTo(this.targetEntity.getPos().getX(), this.targetEntity.getPos().getY(), this.targetEntity.getPos().getZ());
                        }
                    }
                    this.ticksSinceUnseenTeleport = 0;
                } else if (this.targetEntity.squaredDistanceTo(this.baseHerobrineEntity) > 256.0 && this.ticksSinceUnseenTeleport++ >= this.getTickCount(30) && this.baseHerobrineEntity.teleportTo(this.targetEntity)) {
                    this.baseHerobrineEntity.world.playSound(this.baseHerobrineEntity.getX(), this.baseHerobrineEntity.getEyeY(), this.baseHerobrineEntity.getZ(), new BiomeMoodSound(SoundEvents.AMBIENT_CAVE, 0, 8, 2.0).getSound().value(), SoundCategory.HOSTILE, 2.5f, 1.0f, false);
                    this.ticksSinceUnseenTeleport = 0;
                }
            }
            super.tick();
        }
    }

    public static DefaultAttributeContainer.Builder createHerobrineAttributes() {
        return createLivingAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_ARMOR, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, Float.MIN_VALUE)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, Float.MAX_VALUE);
    }
}
