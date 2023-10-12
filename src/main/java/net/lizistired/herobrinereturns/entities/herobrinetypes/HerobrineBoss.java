package net.lizistired.herobrinereturns.entities.herobrinetypes;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.lizistired.herobrinereturns.HerobrineReturns;
import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import static net.lizistired.herobrinereturns.HerobrineReturns.minecraftServer;

public class HerobrineBoss extends BaseHerobrineEntity {
    private static final TrackedData<Integer> TRACKED_ENTITY_ID_1 = DataTracker.registerData(HerobrineBoss.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TRACKED_ENTITY_ID_2 = DataTracker.registerData(HerobrineBoss.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TRACKED_ENTITY_ID_3 = DataTracker.registerData(HerobrineBoss.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TRACKED_ENTITY_ID_4 = DataTracker.registerData(HerobrineBoss.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TRACKED_ENTITY_ID_5 = DataTracker.registerData(HerobrineBoss.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TRACKED_ENTITY_ID_6 = DataTracker.registerData(HerobrineBoss.class, TrackedDataHandlerRegistry.INTEGER);
    private static final List<TrackedData<Integer>> TRACKED_ENTITY_IDS = ImmutableList.of(TRACKED_ENTITY_ID_1, TRACKED_ENTITY_ID_2, TRACKED_ENTITY_ID_3, TRACKED_ENTITY_ID_4, TRACKED_ENTITY_ID_5, TRACKED_ENTITY_ID_6);

    private static final TrackedData<Byte> SPELL = DataTracker.registerData(HerobrineBoss.class, TrackedDataHandlerRegistry.BYTE);
    protected int spellTicks;
    private Spell spell = Spell.NONE;
    public static UUID[] decoyIDs = new UUID[7];
    public static final float scale = 1f;
    public HerobrineBoss(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static Method method = null;
    public static Method method2 = null;
    private final ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS).setDarkenSky(true);

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 1.7400001666666667f * scale;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TRACKED_ENTITY_ID_1, 0);
        this.dataTracker.startTracking(TRACKED_ENTITY_ID_2, 0);
        this.dataTracker.startTracking(TRACKED_ENTITY_ID_3, 0);
        this.dataTracker.startTracking(TRACKED_ENTITY_ID_4, 0);
        this.dataTracker.startTracking(TRACKED_ENTITY_ID_5, 0);
        this.dataTracker.startTracking(TRACKED_ENTITY_ID_6, 0);
        this.dataTracker.startTracking(SPELL, (byte)0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.spellTicks = nbt.getInt("SpellTicks");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("SpellTicks", this.spellTicks);
    }

    public void onSummoned() {
        if(this.world.isClient){
            return;
        }
        this.bossBar.setPercent(0.0f);
        this.setHealth(this.getMaxHealth());
        minecraftServer.getOverworld().setWeather(0, 2400, true, true);
    }

    public State getState() {
        if (this.isSpellcasting()) {
            return State.SPELLCASTING;
        }
        return State.NEUTRAL;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
        this.dataTracker.set(SPELL, (byte)spell.id);
    }

    public Spell getSpell() {
        if (!this.world.isClient) {
            return this.spell;
        }
        return Spell.byId(this.dataTracker.get(SPELL).byteValue());
    }

    public boolean isSpellcasting(){
        if(this.world.isClient){
            return this.dataTracker.get(SPELL) > 0;
        }
        return this.spellTicks > 0;
    }

    @Override
    public void tick(){
        super.tick();
        this.bossBar.setVisible(!this.dead);
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.world.isClient && this.isSpellcasting()) {
            Spell spell = this.getSpell();
        }

    }

    @Override
    protected void mobTick(){
        super.mobTick();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }
        //deltaTick++;
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }
    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        onSummoned();
    }

    @Override
    public void onDeath(DamageSource damageSource){
        if(damageSource == getDamageSources().outOfWorld()){
            return;
        }
        for (ServerPlayerEntity serverPlayerEntity : minecraftServer.getOverworld().getNonSpectatingEntities(ServerPlayerEntity.class, this.getBoundingBox().expand(50.0))) {
            Criteria.PLAYER_KILLED_ENTITY.trigger(serverPlayerEntity, this, damageSource);

        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.world.isClient()){
            return false;
        }
        int range = 256;
        //if (source.isOf(DamageTypes.OUT_OF_WORLD)) {
        //    return false;
        //}
        if(source.isOf(DamageTypes.PLAYER_ATTACK)) {
            //HerobrineReturns.minecraftServer.getOverworld().getEntitiesByClass(DecoyHerobrineEntity.class, new Box(this.getBlockPos().add(-10, -10, -10), this.getBlockPos().add(10, 10, 10)), null).forEach(Entity::discard);
            minecraftServer.getOverworld().getEntitiesByType(RegisterEntities.DECOY_HEROBRINE_ENTITY, new Box(this.getBlockPos().add(-100, -100, -100), this.getBlockPos().add(100, 100, 100)), decoyHerobrineEntity -> true).forEach(Entity::discard);
            //for (int i = 0; i < 6; i++) {
            //    if (HerobrineReturns.minecraftServer.getOverworld().getEntity(decoyIDs[i]) != null){
            //    HerobrineReturns.minecraftServer.getOverworld().getEntity(decoyIDs[i])
            //            .kill();
            //    }
            //}
        }
        if (source.isOf(DamageTypes.LIGHTNING_BOLT)) {
            return false;
        }
        return super.damage(source, amount);
    }

    protected static enum Spell {
        NONE(0, 0.0, 0.0, 0.0),
        SUMMON_VEX(1, 0.7, 0.7, 0.8),
        FANGS(2, 0.4, 0.3, 0.35),
        WOLOLO(3, 0.7, 0.5, 0.2),
        DISAPPEAR(4, 0.3, 0.3, 0.8),
        BLINDNESS(5, 0.1, 0.1, 0.2);

        private static final IntFunction<Spell> BY_ID;
        final int id;
        final double[] particleVelocity;

        private Spell(int id, double particleVelocityX, double particleVelocityY, double particleVelocityZ) {
            this.id = id;
            this.particleVelocity = new double[]{particleVelocityX, particleVelocityY, particleVelocityZ};
        }

        public static Spell byId(int id) {
            return BY_ID.apply(id);
        }

        static {
            BY_ID = ValueLists.createIdToValueFunction((ToIntFunction<Spell>) spell -> spell.id, Spell.values(), ValueLists.OutOfBoundsHandling.ZERO);
        }
    }

    @Override
    protected void initCustomGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.4f, true));
        //this.goalSelector.add(1, new LightningAttack(this, 50, 3));
        this.goalSelector.add(2, new LongLightning(this, 200, 100));
        //this.goalSelector.add(2, new DecoyAttack(this, 50, 3));
        this.targetSelector.add(1, new ActiveTargetGoal<PlayerEntity>((MobEntity) this, PlayerEntity.class, false));
    }

    public static enum State {
        ATTACKING,
        SPELLCASTING,
        NEUTRAL;
    }

    class LightningAttack extends Goal {
        private final HerobrineBoss herobrineBoss1;
        private int ticksSinceAttackStarted;
        private int ticksSinceAttackEnded;
        private int attackDowntime;
        private int attackLength;
        @Nullable
        private LivingEntity target;

        public LightningAttack(HerobrineBoss herobrineBoss1, int attackDowntime, int attackLength) {
            this.herobrineBoss1 = herobrineBoss1;
            this.target = herobrineBoss1.getTarget();
            this.attackDowntime = attackDowntime;
            this.attackLength = attackLength;
        }

        protected int getSpellTicks() {
            return 40;
        }

        protected int startTimeDelay() {
            return 100;
        }
        @Override
        public boolean canStart() {
            this.target = this.herobrineBoss1.getTarget();
            if (!(this.target instanceof PlayerEntity)) {
                return false;
            }
            double d = this.target.squaredDistanceTo(this.herobrineBoss1);
            return !(d > (256 * 10.0));
            //this.herobrineBoss.isPlayerStaring((PlayerEntity) this.target);
        }

        public void start(){
            herobrineBoss1.setNoGravity(true);
            this.ticksSinceAttackStarted = 0;
            this.ticksSinceAttackEnded = 0;
            HerobrineBoss.this.setSpell(Spell.SUMMON_VEX);
            }

        public boolean canStop(){
            HerobrineReturns.LOGGER.info("canStop");
            return ticksSinceAttackStarted > this.attackLength;
        }

        public void stop(){
            HerobrineBoss.this.setSpell(Spell.NONE);
        }

        @Override
        public void tick(){
            if (target.world.isClient()) {
                return;
            }
            //set item display entity to display item diamond sword



            if(ticksSinceAttackStarted < this.attackLength) {
                HerobrineBoss.this.setSpell(Spell.SUMMON_VEX);
                double d = herobrineBoss1.getX() + (Random.create().nextDouble() - 0.5) * 16.0;
                double e = herobrineBoss1.getY() + (double) (Random.create().nextInt(5));
                double f = herobrineBoss1.getZ() + (Random.create().nextDouble() - 0.5) * 16.0;
                LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, target.world); // Create the lightning bolt
                lightning.setPosition(d, e, f); // Set its position. This will make the lightning bolt strike the player (probably not what you want)
                target.world.spawnEntity(lightning); // Spawn the lightning entity
                ticksSinceAttackStarted++;

                //DisplayEntity.ItemDisplayEntity itemDisplayEntity = new DisplayEntity.ItemDisplayEntity(EntityType.ITEM_DISPLAY, target.world);
            } else {
                stop();
            }
            if (this.ticksSinceAttackEnded > this.attackDowntime) {
                this.ticksSinceAttackEnded = 0;
                this.ticksSinceAttackStarted = 0;
            }
            ticksSinceAttackEnded++;
            //super.tick();
            //get the position of the item entity and set the position of the Herobrine boss to that position
            //this.herobrineBoss.world.spawnEntity((EntityType.LIGHTNING_BOLT.create(this.herobrineBoss.world)).setPos(target.getX(), target.getY(), target.getZ()));
        }
    }

    class LongLightning extends Goal {
        private final HerobrineBoss herobrineBoss1;
        private int ticksSinceAttackStarted;
        private int ticksSinceAttackEnded;
        private int attackDowntime;
        private int attackLength;
        @Nullable
        private LivingEntity target;

        public LongLightning(HerobrineBoss herobrineBoss1, int attackDowntime, int attackLength) {
            this.herobrineBoss1 = herobrineBoss1;
            this.target = herobrineBoss1.getTarget();
            this.attackDowntime = attackDowntime;
            this.attackLength = attackLength;
        }

        protected int getSpellTicks() {
            return 40;
        }

        protected int startTimeDelay() {
            return 100;
        }
        @Override
        public boolean canStart() {
            this.target = this.herobrineBoss1.getTarget();
            if (!(this.target instanceof PlayerEntity)) {
                return false;
            }
            double d = this.target.squaredDistanceTo(this.herobrineBoss1);
            return !(d > (256 * 10.0));
            //this.herobrineBoss.isPlayerStaring((PlayerEntity) this.target);
        }

        public void start(){
            herobrineBoss1.setNoGravity(true);
            this.ticksSinceAttackStarted = 0;
            this.ticksSinceAttackEnded = 0;
            HerobrineBoss.this.setSpell(Spell.SUMMON_VEX);

        }

        public boolean canStop(){
            HerobrineReturns.LOGGER.info("canStop");
            return ticksSinceAttackStarted > this.attackLength;
        }

        public void stop(){
            HerobrineBoss.this.setSpell(Spell.NONE);
        }

        @Override
        public void tick(){
            if (target.world.isClient()) {
                return;
            }
            //set item display entity to display item diamond sword



            if(ticksSinceAttackStarted < this.attackLength) {
                //setPosition(herobrineBoss1.getX(), MathHelper.clampedLerp(target.getY(), target.getY() + 15d, HerobrineReturnsClient.context.tickDelta() * 1), herobrineBoss1.getZ());
                HerobrineBoss.this.setSpell(Spell.SUMMON_VEX);
                double d = herobrineBoss1.getX() + (Random.create().nextDouble() - 0.5) * 16.0;
                double e = herobrineBoss1.getY() + (double) (Random.create().nextInt(5));
                double f = herobrineBoss1.getZ() + (Random.create().nextDouble() - 0.5) * 16.0;
                LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, target.world); // Create the lightning bolt
                lightning.setPosition(d, e, f); // Set its position. This will make the lightning bolt strike the player (probably not what you want)
                target.world.spawnEntity(lightning); // Spawn the lightning entity
                ticksSinceAttackStarted++;

                //DisplayEntity.ItemDisplayEntity itemDisplayEntity = new DisplayEntity.ItemDisplayEntity(EntityType.ITEM_DISPLAY, target.world);
            } else {
                stop();
            }
            if (this.ticksSinceAttackEnded > this.attackDowntime) {
                this.ticksSinceAttackEnded = 0;
                this.ticksSinceAttackStarted = 0;
            }
            ticksSinceAttackEnded++;
            //super.tick();
            //get the position of the item entity and set the position of the Herobrine boss to that position
            //this.herobrineBoss.world.spawnEntity((EntityType.LIGHTNING_BOLT.create(this.herobrineBoss.world)).setPos(target.getX(), target.getY(), target.getZ()));
        }
    }

    static class SwordAttack extends Goal{
        private final HerobrineBoss herobrineBoss;
        private int ticksSinceAttackStarted;
        private int ticksSinceAttackEnded;
        private int attackDowntime = 50;
        private int attackLength = 3;
        @Nullable
        private LivingEntity target;

        public SwordAttack(HerobrineBoss herobrineBoss,  int attackDowntime, int attackLength) {
            this.herobrineBoss = herobrineBoss;
            this.target = herobrineBoss.getTarget();
            this.attackDowntime = attackDowntime;
            this.attackLength = attackLength;
        }


        @Override
        public boolean canStart() {
            this.target = this.herobrineBoss.getTarget();
            if (!(this.target instanceof PlayerEntity)) {
                return false;
            }
            double d = this.target.squaredDistanceTo(this.herobrineBoss);
            if (d > (256.0)) {
                return false;
            }
            return true;
        }

        @Override
        public void start(){
            this.ticksSinceAttackStarted = 0;
            this.ticksSinceAttackEnded = 0;
            herobrineBoss.equipStack(EquipmentSlot.MAINHAND, Items.DIAMOND_SWORD.getDefaultStack());
            target.getWorld().playSound(target, BlockPos.ofFloored(target.getPos()), SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.HOSTILE, 1.0f, 1.0f);
            herobrineBoss.world.addParticle(ParticleTypes.ELECTRIC_SPARK, herobrineBoss.getX(), herobrineBoss.getY(), herobrineBoss.getZ(), 0.0D, 0.0D, 0.0D);
        }

        @Override
        public boolean canStop() {
            canStart();
            double d = this.target.squaredDistanceTo(this.herobrineBoss);
            if (d > (256.0)) {
                return false;
            }
            return super.canStop();
        }

        @Override
        public void stop() {
            herobrineBoss.equipStack(EquipmentSlot.MAINHAND, Items.AIR.getDefaultStack());
        }

        @Override
        public void tick(){
            if (target.world.isClient()) {
                return;
            }
            if(ticksSinceAttackStarted < this.attackLength) {
                ticksSinceAttackStarted++;
            } else {
                stop();
            }
            if (this.ticksSinceAttackEnded > this.attackDowntime) {
                this.ticksSinceAttackEnded = 0;
                this.ticksSinceAttackStarted = 0;
            }
            super.tick();
        }
    }

    class DecoyAttack extends Goal {
        private final HerobrineBoss herobrineBoss;
        private int ticksSinceAttackStarted;
        private int ticksSinceAttackEnded;
        private int attackDowntime = 50;
        private int attackLength = 3;

        int heroBrinesSpawned = 0;
        Boolean[] decoyAlive = new Boolean[8];



        protected int getSpellTicks() {
            return 40;
        }

        protected int startTimeDelay() {
            return 100;
        }

        @Nullable
        private LivingEntity target;
        public DecoyAttack(HerobrineBoss herobrineBoss,  int attackDowntime, int attackLength) {
            this.herobrineBoss = herobrineBoss;
            this.target = herobrineBoss.getTarget();
            this.attackDowntime = attackDowntime;
            this.attackLength = attackLength;
        }

        @Override
        public boolean canStart() {
            this.target = this.herobrineBoss.getTarget();
            if (!(this.target instanceof PlayerEntity)) {
                return false;
            }
            double d = this.target.squaredDistanceTo(this.herobrineBoss);
            return !(d > (256.0 * 10)) && !areDecoysAlive(decoyIDs);
        }

        @Override
        public void start(){
            HerobrineBoss.this.setSpell(Spell.SUMMON_VEX);
            int herobrineSpawned = 0;
            int numberOfDecoys = 7;
            for(int i = 0; i < numberOfDecoys; i++){

                double d = target.getX() + (Random.create().nextDouble() - 0.5) * 16.0;
                double e = target.getY();
                double f = target.getZ() + (Random.create().nextDouble() - 0.5) * 16.0;
                //create a new decoy entity
                DecoyHerobrineEntity decoyHerobrineEntity = new DecoyHerobrineEntity(RegisterEntities.DECOY_HEROBRINE_ENTITY, this.target.world);
                //set the position of the decoy entity to the position of the Herobrine boss
                decoyHerobrineEntity.setPosition(d,e,f);
                decoyHerobrineEntity.equipStack(EquipmentSlot.MAINHAND, Items.BOW.getDefaultStack());
                //spawn the decoy entity
                decoyIDs[i] = decoyHerobrineEntity.getUuid();
                herobrineBoss.world.spawnEntity(decoyHerobrineEntity);

                this.heroBrinesSpawned++;
            }
            this.ticksSinceAttackStarted = 0;
            this.ticksSinceAttackEnded = 0;
        }


        @Override
        public void tick() {
            super.tick();
        }

        @Override
        public boolean canStop() {
            return this.heroBrinesSpawned > 7;
        }
        @Override
        public void stop(){
            HerobrineBoss.this.setSpell(Spell.NONE);
            if (!minecraftServer.getOverworld().getEntitiesByType(RegisterEntities.DECOY_HEROBRINE_ENTITY, new Box(this.herobrineBoss.getBlockPos().add(-100, -100, -100), this.herobrineBoss.getBlockPos().add(100, 100, 100)), decoyHerobrineEntity -> true).isEmpty()){
                minecraftServer.getOverworld().getEntitiesByType(RegisterEntities.DECOY_HEROBRINE_ENTITY, new Box(this.herobrineBoss.getBlockPos().add(-100, -100, -100), this.herobrineBoss.getBlockPos().add(100, 100, 100)), decoyHerobrineEntity -> true).forEach(Entity::discard);

            }
            //for (int i = 0; i < 7; i++) {
            //    if (HerobrineReturns.minecraftServer.getOverworld().getEntity(decoyIDs[i]) != null){
            //        HerobrineReturns.minecraftServer.getOverworld().getEntity(decoyIDs[i])
            //                .kill();
            //    }
            //}
        }

        boolean decoyPhase(){
            return true;
        }

        boolean areDecoysAlive(UUID[] array) {
                if (array.length == 0) {
                    for (int i = 0; i < array.length; i++) {
                        if ((minecraftServer.getOverworld().getEntity(array[i]) == null)) {
                            decoyAlive[i] = Boolean.TRUE;
                            HerobrineReturns.LOGGER.info(String.valueOf(new ArrayList<>(List.of(array))));
                        }
                    }
                }
                if (new ArrayList<>(Arrays.asList(decoyAlive)).contains(true)){
                    for (int i = 0; i < decoyAlive.length; i++) {
                        HerobrineReturns.LOGGER.info(String.valueOf(new ArrayList<>(Arrays.asList(decoyAlive))));
                    }
                    return false;
                }
                return false;
            }
        }

    public static class MeleeAttackGoal
            extends Goal {

        protected final PathAwareEntity mob;
        private final double speed;
        private final boolean pauseWhenMobIdle;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int updateCountdownTicks;
        private int cooldown;
        private final int attackIntervalTicks = 20;
        private long lastUpdateTime;
        private static final long MAX_ATTACK_TIME = 20L;

        public MeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            this.mob = mob;
            this.speed = speed;
            this.pauseWhenMobIdle = pauseWhenMobIdle;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            long l = this.mob.world.getTime();
            if (l - this.lastUpdateTime < 20L) {
                return false;
            }
            this.lastUpdateTime = l;
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            this.path = this.mob.getNavigation().findPathTo(livingEntity, 0);
            if (this.path != null) {
                return true;
            }
            return this.getSquaredMaxAttackDistance(livingEntity) >= this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (!this.pauseWhenMobIdle) {
                return !this.mob.getNavigation().isIdle();
            }
            if (!this.mob.isInWalkTargetRange(livingEntity.getBlockPos())) {
                return false;
            }
            return !(livingEntity instanceof PlayerEntity) || !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative();
        }

        @Override
        public void start() {
            this.mob.getNavigation().startMovingAlong(this.path, this.speed);
            this.mob.setAttacking(true);
            this.mob.equipStack(EquipmentSlot.MAINHAND, Items.DIAMOND_SWORD.getDefaultStack());
            this.updateCountdownTicks = 0;
            this.cooldown = 0;
        }

        @Override
        public void stop() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.mob.setTarget(null);
            }
            this.mob.setAttacking(false);
            this.mob.getNavigation().stop();
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return;
            }
            this.mob.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
            double d = this.mob.getSquaredDistanceToAttackPosOf(livingEntity);
            this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
            if ((this.pauseWhenMobIdle || this.mob.getVisibilityCache().canSee(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                this.targetX = livingEntity.getX();
                this.targetY = livingEntity.getY();
                this.targetZ = livingEntity.getZ();
                this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
                if (d > 1024.0) {
                    this.updateCountdownTicks += 10;
                } else if (d > 256.0) {
                    this.updateCountdownTicks += 5;
                }
                if (!this.mob.getNavigation().startMovingTo(livingEntity, this.speed)) {
                    this.updateCountdownTicks += 15;
                }
                this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
            }
            this.cooldown = Math.max(this.cooldown - 1, 0);
            this.attack(livingEntity, d);
        }

        protected void attack(LivingEntity target, double squaredDistance) {
            double d = this.getSquaredMaxAttackDistance(target);
            if (squaredDistance <= d && this.cooldown <= 0) {
                this.resetCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);
                this.mob.tryAttack(target);
            }
        }

        protected void resetCooldown() {
            this.cooldown = this.getTickCount(20);
        }

        protected boolean isCooledDown() {
            return this.cooldown <= 0;
        }

        protected int getCooldown() {
            return this.cooldown;
        }

        protected int getMaxCooldown() {
            return this.getTickCount(20);
        }

        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f) + entity.getWidth();
        }
    }


    public static DefaultAttributeContainer.Builder createHerobrineAttributes() {
        return createLivingAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 1024)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 500)
                .add(EntityAttributes.GENERIC_ARMOR)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, Float.MAX_VALUE)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
    }
}
