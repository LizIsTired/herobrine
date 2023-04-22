package net.lizistired.herobrinereturns.utils.misc;

import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class RapidTitle {
    static final Random random = Random.create();

    public static TitleS2CPacket title(String key, int min, int max, int fadeIn, int stay, int fadeOut){
        String textToEdit = key + random.nextBetween(min, max);
        //textToEdit.
        return new TitleS2CPacket(Text.translatable(key + random.nextBetween(min, max)));
    }

    // Main driver method
    public static String main(String[] args, String key)
    {
        // Custom input string
        String str = key;
        String[] arrOfStr = str.split(" ", 5);

        for (String a : arrOfStr)
            return a;
            //System.out.println(a);
        return str;
    }
}
