package net.lizistired.herobrinereturns.utils.registry;

import net.lizistired.herobrinereturns.items.CursedBook;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class RegisterItems {
    static FoodComponent newFoodComponent;
    public static Item CURSED_BOOK = new CursedBook(new Item.Settings().food(new FoodComponent.Builder().hunger(100).saturationModifier(0.3f).build()).maxCount(1).rarity(Rarity.EPIC));
    public static void init(){
        Registry.register(Registries.ITEM, new Identifier("herobrinereturns", "cursed_book"), CURSED_BOOK);

    }
}
