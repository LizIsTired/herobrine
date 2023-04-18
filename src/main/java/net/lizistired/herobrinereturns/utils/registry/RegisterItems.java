package net.lizistired.herobrinereturns.utils.registry;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class RegisterItems {
    public static Item CURSED_BOOK = new Item(new Item.Settings().maxCount(1));
    public static void init(){
        Registry.register(Registries.ITEM, new Identifier("herobrinereturns", "cursed_book"), CURSED_BOOK);
    }
}
