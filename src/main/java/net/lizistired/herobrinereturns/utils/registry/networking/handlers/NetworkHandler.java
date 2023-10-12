package net.lizistired.herobrinereturns.utils.registry.networking.handlers;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lizistired.herobrinereturns.entities.herobrinetypes.ScaryHerobrineEntity;
import net.lizistired.herobrinereturns.entities.renderers.HerobrineScaryRenderer;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.lizistired.herobrinereturns.utils.registry.networking.NetworkConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

import static net.lizistired.herobrinereturns.HerobrineReturns.LOGGER;

public class NetworkHandler {

    public static UUID uuid;
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkConstants.PACKET_ID_HEROBRINE, (server, player, handler, buf, responseSender) -> {

            server.execute(() ->	{
                PlayerLookup.all(server).forEach((playerEntity) -> {
                    new Thread(() -> {
                        executeHerobrine(server, playerEntity);
                    }).start();
                });
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(NetworkConstants.PACKET_ID_HEROBRINE_D, (server, player, handler, buf, responseSender) -> {

            server.execute(() ->	{
                        server.getOverworld().getEntity(uuid).discard();
            });
        });
    }

    static void executeHerobrine(MinecraftServer server, ServerPlayerEntity playerEntity){
        ScaryHerobrineEntity entity = new ScaryHerobrineEntity(RegisterEntities.SCARY_HEROBRINE, server.getOverworld());
        //spawn entity 10 blocks in front of player
        float x = MinecraftClient.getInstance().player.getYaw();
        float y = MinecraftClient.getInstance().player.getPitch();
        Vec3d forward = Vec3d.fromPolar(y, x);
        //entity.setPos(playerEntity.getPos().getX() + playerEntity.getRotationVector().getX() * 10, playerEntity.getPos().getY() + playerEntity.getRotationVector().getY() * 10, playerEntity.getPos().getZ() + playerEntity.getRotationVector().getZ() * 10);
        entity.setPos(playerEntity.getPos().getX() + forward.x * 10, playerEntity.getPos().getY() + forward.y * 10, playerEntity.getPos().getZ() + forward.z * 10);
        LOGGER.info(forward.toString());
        entity.setCustomName(Text.translatable("entity.minecraft.herobrinenametag"));
        entity.setNoGravity(true);
        server.getOverworld().spawnEntity(entity);
        uuid = entity.getUuid();
        playerEntity.sendMessage(Text.of("Herobrine is here!"), false);
    }

}
