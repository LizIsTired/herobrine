package net.lizistired.herobrinereturns.mixin;

import net.lizistired.herobrinereturns.HerobrineReturns;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotMixin {

    @Inject(at = @At("HEAD"), method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V")
    private static void saveScreenshot(File gameDirectory, String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) {
        HerobrineReturns.LOGGER.info("Screenshot saved!");
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.player.getRotationVector();
        Vec3d pos = minecraftClient.player.getPos();
        double d = pos.getX() + (Random.create().nextDouble() - 0.5) * 64.0;
        double e = pos.getY() + (double)(Random.create().nextInt(64) - 32);
        double f = pos.getZ() + (Random.create().nextDouble() - 0.5) * 64.0;

    }

}
