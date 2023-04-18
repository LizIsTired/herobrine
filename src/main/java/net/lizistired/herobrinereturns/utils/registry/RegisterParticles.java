package net.lizistired.herobrinereturns.utils.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class RegisterParticles {
    public static final DefaultParticleType HEROBRINE_JUMPSCARE = FabricParticleTypes.simple();
    public static final DefaultParticleType HEROBRINE_JUMPSCARE_GOOFY_AHHH = FabricParticleTypes.simple();
    public static void init(){
        Registry.register(Registries.PARTICLE_TYPE, new Identifier("minecraft", "herobrinejumpscare"), HEROBRINE_JUMPSCARE);
        //Registry.register(Registries.PARTICLE_TYPE, new Identifier("minecraft", "herobrinejumpscaregoofyahhh"), HEROBRINE_JUMPSCARE_GOOFY_AHHH).shouldAlwaysSpawn();
    }
}
