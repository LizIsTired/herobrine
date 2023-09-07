package net.lizistired.herobrinereturns.entities.herobrinetypes;

import com.ibm.icu.impl.SortedSetRelation;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.State;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public abstract class SpellcastingHerobrineEntity extends BaseHerobrineEntity {

    private static final TrackedData<Byte> SPELL = DataTracker.registerData(SpellcastingHerobrineEntity.class, TrackedDataHandlerRegistry.BYTE);
    protected int spellTicks;
    private Spell spell = Spell.NONE;

    protected SpellcastingHerobrineEntity(EntityType<? extends SpellcastingHerobrineEntity> entityType, World world) {
        super((EntityType<? extends SpellcastingHerobrineEntity>)entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
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


    public State getState() {
        if (this.isSpellcasting()) {
            return State.SPELLCASTING;
        }
        return State.NEUTRAL;
    }

    public static enum State {
        ATTACKING,
        SPELLCASTING,
        NEUTRAL;
    }

    public boolean isSpellcasting() {
        if (this.world.isClient) {
            return this.dataTracker.get(SPELL) > 0;
        }
        return this.spellTicks > 0;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
        this.dataTracker.set(SPELL, (byte)spell.id);
    }

    protected Spell getSpell() {
        if (!this.world.isClient) {
            return this.spell;
        }
        return Spell.byId(this.dataTracker.get(SPELL).byteValue());
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClient && this.isSpellcasting()) {
            Spell spell = this.getSpell();
            double d = spell.particleVelocity[0];
            double e = spell.particleVelocity[1];
            double f = spell.particleVelocity[2];
            float g = this.bodyYaw * ((float)Math.PI / 180) + MathHelper.cos((float)this.age * 0.6662f) * 0.25f;
            float h = MathHelper.cos(g);
            float i = MathHelper.sin(g);
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double)h * 0.6, this.getY() + 1.8, this.getZ() + (double)i * 0.6, d, e, f);
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double)h * 0.6, this.getY() + 1.8, this.getZ() - (double)i * 0.6, d, e, f);
        }
    }

    protected int getSpellTicks() {
        return this.spellTicks;
    }

    protected abstract SoundEvent getCastSpellSound();

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

    protected abstract class CastSpellGoal
            extends Goal {
        protected int spellCooldown;
        protected int startTime;

        protected CastSpellGoal() {
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = SpellcastingHerobrineEntity.this.getTarget();
            if (livingEntity == null || !livingEntity.isAlive()) {
                return false;
            }
            if (SpellcastingHerobrineEntity.this.isSpellcasting()) {
                return false;
            }
            return SpellcastingHerobrineEntity.this.age >= this.startTime;
        }


        @Override
        public boolean shouldContinue() {
            LivingEntity livingEntity = SpellcastingHerobrineEntity.this.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.spellCooldown > 0;
        }

        @Override
        public void start() {
            this.spellCooldown = this.getTickCount(this.getInitialCooldown());
            SpellcastingHerobrineEntity.this.spellTicks = this.getSpellTicks();
            this.startTime = SpellcastingHerobrineEntity.this.age + this.startTimeDelay();
            SoundEvent soundEvent = this.getSoundPrepare();
            if (soundEvent != null) {
                SpellcastingHerobrineEntity.this.playSound(soundEvent, 1.0f, 1.0f);
            }
            SpellcastingHerobrineEntity.this.setSpell(this.getSpell());
        }

        @Override
        public void tick() {
            --this.spellCooldown;
            if (this.spellCooldown == 0) {
                this.castSpell();
                SpellcastingHerobrineEntity.this.playSound(SpellcastingHerobrineEntity.this.getCastSpellSound(), 1.0f, 1.0f);
            }
        }

        protected abstract void castSpell();

        protected int getInitialCooldown() {
            return 20;
        }

        protected abstract int getSpellTicks();

        protected abstract int startTimeDelay();

        @Nullable
        protected abstract SoundEvent getSoundPrepare();

        protected abstract Spell getSpell();
    }
}

