package net.lizistired.herobrinereturns.entities.herobrinetypes;

import net.lizistired.herobrinereturns.HerobrineReturns;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.lizistired.herobrinereturns.utils.misc.RapidTitle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.lizistired.herobrinereturns.HerobrineReturns.minecraftServer;

public class DecoyHerobrineEntity extends BaseHerobrineEntity implements Angerable {

    public DecoyHerobrineEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 500.0f, 1.0f));
        //this.goalSelector.add(1, new EatGrassGoal(this));
        //this.goalSelector.add(1, new FlyGoal(this, 500));
        //this.goalSelector.add(8, new WanderAroundGoal(this, 0.23f));
        //this.goalSelector.add(8, new LookAroundGoal(this));
        this.initCustomGoals();
    }

    @Override
    public boolean shouldDropXp(){
        return false;
    }

    protected void initCustomGoals() {
        this.goalSelector.add(2, new MeleeAttackGoal(this, 0.4f, false));
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity) this, PlayerEntity.class, true));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        //if (!source.isOf(DamageTypes.OUT_OF_WORLD)) {
        //    //if (Objects.equals(source.getName(), "player")) {
        //    //    //source.getSource().addVelocity(500,5,0);
        //    //    //source.getSource().startRiding(this, true);
        //    //}
        //    //return false;
        //}
        this.discard();
        return super.damage(source, amount);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        return entityData;
    }

    //@Override
   //protected void mobTick() {
   //    super.mobTick();
   //    if (this.getTarget() instanceof PlayerEntity) {
   //        //if ((this.age + this.getId()) % 1200 == 0) {
   //        if (isPlayerStaring((PlayerEntity) this.getTarget()) && this.age > 15) {

   //            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.SLOWNESS, 50, 0);
   //            StatusEffectInstance statusEffectInstance1 = new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 256);
   //            StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) this.world, this, this.getPos(), 50.0, statusEffectInstance, 50);
   //            List<ServerPlayerEntity> list = StatusEffectUtil.addEffectToPlayersWithinDistance((ServerWorld) this.world, this, this.getPos(), 50.0, statusEffectInstance1, 5);
   //            list.forEach(serverPlayerEntity -> serverPlayerEntity.networkHandler.sendPacket(new GameStateChangeS2CPacket(HerobrineReturns.HEROBRINE_APPEARANCE_EFFECT, this.isSilent() ? GameStateChangeS2CPacket.DEMO_OPEN_SCREEN : (int) 1.0f)));
   //            list.forEach(serverPlayerEntity -> serverPlayerEntity.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0f, 1.0f));
   //            //list.forEach(serverPlayerEntity -> serverPlayerEntity.sendMessage(Text.translatable("entity.minecraft.herobrine.chat" + random.nextBetween(1, 7)), true));
   //            list.forEach(serverPlayerEntity -> serverPlayerEntity.networkHandler.sendPacket(new TitleFadeS2CPacket(5, 2, 5)));
   //            list.forEach(serverPlayerEntity -> serverPlayerEntity.networkHandler.sendPacket(RapidTitle.title("entity.minecraft.herobrine.chat", 1, 7, 5, 2, 5)));
   //            this.age = 0;
   //            //}
   //        }
   //    }
   //    if (!this.hasPositionTarget()) {
   //        this.setPositionTarget(this.getBlockPos(), 16);
   //    }
   //}

    //public boolean isPlayerStaring(@NotNull PlayerEntity player) {
    //    Vec3d vec3d = player.getRotationVec(1.0f).normalize();
    //    Vec3d vec3d2 = new Vec3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
    //    double d = vec3d2.length();
    //    double e = vec3d.dotProduct(vec3d2 = vec3d2.normalize());
    //    if (e > 1.0 - 0.025 / d) {
    //        return player.canSee(this);
    //    }
    //    return false;
    //}


    public static DefaultAttributeContainer.Builder createHerobrineAttributes() {
        return createLivingAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, Float.MIN_VALUE)
                .add(EntityAttributes.GENERIC_ARMOR, Float.MIN_VALUE)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, Float.MIN_VALUE)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, Float.MIN_VALUE)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, Float.MIN_VALUE);
    }
}
