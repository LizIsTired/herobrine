package net.lizistired.herobrinereturns.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CursedBook extends Item {
    public CursedBook(Settings settings) {
        super(settings);
    }
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
