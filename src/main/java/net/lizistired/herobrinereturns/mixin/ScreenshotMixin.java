package net.lizistired.herobrinereturns.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.lizistired.herobrinereturns.HerobrineReturns;
import net.lizistired.herobrinereturns.utils.registry.networking.NetworkConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotMixin {

    @Inject(at = @At("HEAD"), method = "saveScreenshot(Ljava/io/File;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V", cancellable = true)
    private static void saveScreenshot(File gameDirectory, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) throws IOException {
        Framebuffer framebuffer1 = new SimpleFramebuffer(framebuffer.textureWidth, framebuffer.textureHeight, true, MinecraftClient.IS_SYSTEM_MAC);
        framebuffer1.beginWrite(true);
        framebuffer1.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        //MinecraftClient.getInstance().gameRenderer.renderWorld(MinecraftClient.getInstance().getTickDelta(), Util.getMeasuringTimeNano(), new MatrixStack());
        //MinecraftClient.getInstance().gameRenderer.render(MinecraftClient.getInstance().getTickDelta(), Util.getMeasuringTimeNano(), true);
        WorldRendererInvoker worldRendererInvoker = (WorldRendererInvoker) MinecraftClient.getInstance().worldRenderer;
        Entity cpe= MinecraftClient.getInstance().cameraEntity;
        MatrixStack matrices = new MatrixStack();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cpe.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cpe.getYaw() + 180.0f));
        worldRendererInvoker.callRenderEntity(MinecraftClient.getInstance().player, cpe.getX(), cpe.getY(), cpe.prevZ, MinecraftClient.getInstance().getTickDelta(), matrices, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers());
        //framebuffer1.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        framebuffer1.endWrite();
        ClientPlayNetworking.send(NetworkConstants.PACKET_ID_HEROBRINE, PacketByteBufs.empty());
        new Thread(() -> {
            try {
                Thread.sleep(50);
                ScreenshotRecorder.saveScreenshot(gameDirectory, null, framebuffer1, messageReceiver);;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        ci.cancel();
    }
    @Inject(method = "method_1661(Lnet/minecraft/client/texture/NativeImage;Ljava/io/File;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;close()V", shift = At.Shift.AFTER),  cancellable = true)
    private static void saveScreenshotInner(NativeImage nativeImage, File file, Consumer consumer, CallbackInfo ci) {
        ClientPlayNetworking.send(NetworkConstants.PACKET_ID_HEROBRINE_D, PacketByteBufs.empty());
    }

    @Inject(at = @At("HEAD"), method = "takeScreenshot(Lnet/minecraft/client/gl/Framebuffer;)Lnet/minecraft/client/texture/NativeImage;", cancellable = true)
    private static void saveScreenshot(Framebuffer framebuffer, CallbackInfoReturnable<NativeImage> cir) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }

}
