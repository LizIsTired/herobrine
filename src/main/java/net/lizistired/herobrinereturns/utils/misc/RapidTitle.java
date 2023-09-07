package net.lizistired.herobrinereturns.utils.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;

import java.util.Arrays;

public class RapidTitle {
    static final Random random = Random.create();
    public static void function(ServerPlayerEntity player, String key, int fadeIn, int stay, int fadeOut, int duration) throws InterruptedException {
        String str = Text.translatable(key + random.nextBetween(1, 7)).getString();
        String[] arrOfStr = str.split(" ");

        for (String a : arrOfStr) {
            player.networkHandler.sendPacket(new TitleFadeS2CPacket(fadeIn, stay, fadeOut));
            player.networkHandler.sendPacket(new TitleS2CPacket(Text.translatable(a)));
            Thread.sleep(duration);
        }
    }
}
