package net.lizistired.herobrinereturns.mixin;

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

    @Accessor
    MinecraftClient client;

    //@Inject(method = "onGameStateChange(Lnet/minecraft/network/packet/s2c/play/GameStateChangeS2CPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", shift = At.Shift.BY, by = 4))


    //inject into the method onGameStateChange in ClientPlayNetworkHandler, at the last "else if", inject and print to console


    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onGameStateChange(Lnet/minecraft/network/packet/s2c/play/GameStateChangeS2CPacket;)V", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onGameStateChange(GameStateChangeS2CPacket packet, CallbackInfo ci) {
        ((ClientPlayNetworkHandler)(Object)this).client.player);
        ((ClientPlayNetworkHandler)(Object)this).getWorld().addParticle(ParticleTypes.ELDER_GUARDIAN, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 0.0, 0.0, 0.0));
        //System.out.println("Game state changed! Elder Guardian particle effect played from Elder Guardian entity. This is a Herobrine Returns mixin!");
        if (packet.getReason() == GameStateChangeS2CPacket.GAME_MODE_CHANGED) {
            System.out.println("Game state changed! Elder Guardian particle effect played from Elder Guardian entity. This is a Herobrine Returns mixin!");
        }
    }

}
