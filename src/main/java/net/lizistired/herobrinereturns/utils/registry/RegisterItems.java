package net.lizistired.herobrinereturns.utils.registry;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class RegisterItems {
    static FoodComponent newFoodComponent;
    public static Item CURSED_BOOK = new Item(new Item.Settings().maxCount(1).food(FoodComponents.APPLE).rarity(Rarity.EPIC));
    public static void init(){
        FoodComponent newFoodComponent = new FoodComponent.Builder().hunger(4).saturationModifier(0.3f).build();
        Registry.register(Registries.ITEM, new Identifier("herobrinereturns", "cursed_book"), CURSED_BOOK);

    }
}
