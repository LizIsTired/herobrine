package net.lizistired.herobrinereturns.mixin;

import net.lizistired.herobrinereturns.HerobrineReturns;
import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.utils.registry.RegisterParticles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onGameStateChange(Lnet/minecraft/network/packet/s2c/play/GameStateChangeS2CPacket;)V", at = @At(value = "TAIL"))
    public void onGameStateChange(GameStateChangeS2CPacket packet, CallbackInfo ci) {
        if (packet.getReason() == HerobrineReturns.HEROBRINE_APPEARANCE_EFFECT) {
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerEntity playerEntity = client.player;
            client.world.addImportantParticle(RegisterParticles.HEROBRINE_JUMPSCARE, true, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 0.0, 0.0, 0.0);
        }
    }
}
