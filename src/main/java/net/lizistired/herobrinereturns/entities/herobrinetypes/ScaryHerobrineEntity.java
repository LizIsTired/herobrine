package net.lizistired.herobrinereturns.entities.herobrinetypes;

import net.lizistired.herobrinereturns.HerobrineReturns;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.lizistired.herobrinereturns.utils.misc.RapidTitle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

import static net.lizistired.herobrinereturns.HerobrineReturns.minecraftServer;

public class ScaryHerobrineEntity extends HostileEntity {
    protected static final TrackedData<Byte> VEX_FLAGS = DataTracker.registerData(ScaryHerobrineEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> NO_GRAVITY = DataTracker.registerData(ScaryHerobrineEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> NO_DRAG = DataTracker.registerData(ScaryHerobrineEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final int CHARGING_FLAG = 1;
    @Nullable
    private BlockPos bounds;
    public ScaryHerobrineEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new ScaryHerobrineEntity.VexMoveControl(this);
    }

    @Override
    public boolean cannotDespawn() {
        super.cannotDespawn();
        return true;
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
        this.setCustomName(Text.translatable("entity.minecraft.herobrinenametag"));
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VEX_FLAGS, (byte)0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("BoundX")) {
            this.bounds = new BlockPos(nbt.getInt("BoundX"), nbt.getInt("BoundY"), nbt.getInt("BoundZ"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.bounds != null) {
            nbt.putInt("BoundX", this.bounds.getX());
            nbt.putInt("BoundY", this.bounds.getY());
            nbt.putInt("BoundZ", this.bounds.getZ());
        }
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);
        this.checkBlockCollision();
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height - 0.28125f;
    }
    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
        if(this.getTarget() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) this.getTarget();
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            if (ScaryHerobrineEntity.this.getBoundingBox().intersects(player.getBoundingBox())) {
                player.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
                serverPlayerEntity.networkHandler.sendPacket(new GameStateChangeS2CPacket(HerobrineReturns.HEROBRINE_APPEARANCE_EFFECT, this.isSilent() ? GameStateChangeS2CPacket.DEMO_OPEN_SCREEN : (int) 1.0f));
                this.discard();
                }
            }
        }
    @Override
    protected void mobTick(){
    }

    @Nullable
    public BlockPos getBounds() {
        return this.bounds;
    }

    public void setBounds(@Nullable BlockPos bounds) {
        this.bounds = bounds;
    }

    private boolean areFlagsSet(int mask) {
        byte i = this.dataTracker.get(VEX_FLAGS);
        return (i & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value) {
        int i = this.dataTracker.get(VEX_FLAGS).byteValue();
        i = value ? (i |= mask) : (i &= ~mask);
        this.dataTracker.set(VEX_FLAGS, (byte)(i & 0xFF));
    }

    public boolean isCharging() {
        return this.areFlagsSet(CHARGING_FLAG);
    }

    public void setCharging(boolean charging) {
        this.setVexFlag(CHARGING_FLAG, charging);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(4, new ScaryHerobrineEntity.ChargeTargetGoal());
        this.goalSelector.add(8, new ScaryHerobrineEntity.LookAtTargetGoal());
        this.targetSelector.add(3, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));
    }

    @Override
    public double getHeightOffset() {
        return -2f;
    }
    class VexMoveControl
            extends MoveControl {
        public VexMoveControl(ScaryHerobrineEntity owner) {
            super(owner);
        }

        @Override
        public void tick() {
            if (this.state != MoveControl.State.MOVE_TO) {
                return;
            }
            Vec3d vec3d = new Vec3d(this.targetX - ScaryHerobrineEntity.this.getX(), this.targetY - ScaryHerobrineEntity.this.getY(), this.targetZ - ScaryHerobrineEntity.this.getZ());
            double d = vec3d.length();
            if (d < ScaryHerobrineEntity.this.getBoundingBox().getAverageSideLength()) {
                this.state = MoveControl.State.WAIT;
                ScaryHerobrineEntity.this.setVelocity(ScaryHerobrineEntity.this.getVelocity().multiply(0.5));
            } else {
                ScaryHerobrineEntity.this.setVelocity(ScaryHerobrineEntity.this.getVelocity().add(vec3d.multiply(this.speed * 0.05 / d)));
                if (ScaryHerobrineEntity.this.getTarget() == null) {
                    Vec3d vec3d2 = ScaryHerobrineEntity.this.getVelocity();
                    ScaryHerobrineEntity.this.setYaw(-((float) MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776f);
                    ScaryHerobrineEntity.this.bodyYaw = ScaryHerobrineEntity.this.getYaw();
                } else {
                    double e = ScaryHerobrineEntity.this.getTarget().getX() - ScaryHerobrineEntity.this.getX();
                    double f = ScaryHerobrineEntity.this.getTarget().getZ() - ScaryHerobrineEntity.this.getZ();
                    ScaryHerobrineEntity.this.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776f);
                    ScaryHerobrineEntity.this.bodyYaw = ScaryHerobrineEntity.this.getYaw();
                }
            }
        }
    }

    class ChargeTargetGoal
            extends Goal {
        public ChargeTargetGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = ScaryHerobrineEntity.this.getTarget();
            if (livingEntity != null && livingEntity.isAlive() && !ScaryHerobrineEntity.this.getMoveControl().isMoving() && ScaryHerobrineEntity.this.random.nextInt(ScaryHerobrineEntity.ChargeTargetGoal.toGoalTicks(7)) == 0) {
                return ScaryHerobrineEntity.this.squaredDistanceTo(livingEntity) > 4.0;
            }
            return false;
        }

        @Override
        public boolean shouldContinue() {
            return ScaryHerobrineEntity.this.getMoveControl().isMoving() && ScaryHerobrineEntity.this.isCharging() && ScaryHerobrineEntity.this.getTarget() != null && ScaryHerobrineEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            LivingEntity livingEntity = ScaryHerobrineEntity.this.getTarget();
            if (livingEntity != null) {
                Vec3d vec3d = livingEntity.getEyePos();
                ScaryHerobrineEntity.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0);
            }
            ScaryHerobrineEntity.this.setCharging(true);
            ScaryHerobrineEntity.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0f, 1.0f);
        }

        @Override
        public void stop() {
            ScaryHerobrineEntity.this.setCharging(false);
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = ScaryHerobrineEntity.this.getTarget();
            if (livingEntity == null) {
                return;
            }
            if (ScaryHerobrineEntity.this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
                //ScaryHerobrineEntity.this.tryAttack(livingEntity);
                ScaryHerobrineEntity.this.setCharging(false);
            } else {
                double d = ScaryHerobrineEntity.this.squaredDistanceTo(livingEntity);
                if (d < 9.0) {
                    Vec3d vec3d = livingEntity.getEyePos();
                    ScaryHerobrineEntity.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0);
                }
            }
        }
    }

    class LookAtTargetGoal
            extends Goal {
        public LookAtTargetGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return !ScaryHerobrineEntity.this.getMoveControl().isMoving() && ScaryHerobrineEntity.this.random.nextInt(ScaryHerobrineEntity.LookAtTargetGoal.toGoalTicks(7)) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos blockPos = ScaryHerobrineEntity.this.getBounds();
            if (blockPos == null) {
                blockPos = ScaryHerobrineEntity.this.getBlockPos();
            }
            for (int i = 0; i < 3; ++i) {
                BlockPos blockPos2 = blockPos.add(ScaryHerobrineEntity.this.random.nextInt(15) - 7, ScaryHerobrineEntity.this.random.nextInt(11) - 5, ScaryHerobrineEntity.this.random.nextInt(15) - 7);
                if (!ScaryHerobrineEntity.this.world.isAir(blockPos2)) continue;
                ScaryHerobrineEntity.this.moveControl.moveTo((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5, 0.25);
                if (ScaryHerobrineEntity.this.getTarget() != null) break;
                ScaryHerobrineEntity.this.getLookControl().lookAt((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5, 180.0f, 20.0f);
                break;
            }
        }
    }
}

