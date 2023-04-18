package net.lizistired.herobrinereturns.utils.datagen;

import net.lizistired.herobrinereturns.utils.registry.RegisterItems;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class Advancements implements Consumer<Consumer<Advancement>> {

    @Override
    public void accept(Consumer<Advancement> consumer) {
        Advancement rootAdvancement = Advancement.Builder.create()
                .display(
                        Items.BOOK, // The display icon
                        Text.literal("Oh no..."), // The title
                        Text.literal("He's coming now..."), // The description
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"), // Background image used
                        AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .criterion("got_dirt", InventoryChangedCriterion.Conditions.items(RegisterItems.CURSED_BOOK))
                .build(consumer, "herobrinereturns" + "/root");
    }
}
