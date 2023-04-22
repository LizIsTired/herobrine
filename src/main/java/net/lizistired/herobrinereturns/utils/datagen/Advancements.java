package net.lizistired.herobrinereturns.utils.datagen;

import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.lizistired.herobrinereturns.utils.registry.RegisterItems;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.SummonedEntityCriterion;
import net.minecraft.entity.mob.CreeperEntity;
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
                        new Identifier("textures/block/cherry_log.png"), // Background image used
                        AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .criterion("cursed", InventoryChangedCriterion.Conditions.items(RegisterItems.CURSED_BOOK))
                .build(consumer, "herobrinereturns" + "/root");

        Advancement killHerobrine = Advancement.Builder.create().parent(rootAdvancement)
                .display(
                        Items.BOOK, // The display icon
                        Text.literal("You...ate the book??"), // The title
                        Text.literal("...wtf dude"), // The description
                        new Identifier("textures/block/cherry_log.png"), // Background image used
                        AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .criterion("kill_herobrine", ConsumeItemCriterion.Conditions.item(RegisterItems.CURSED_BOOK))
                .build(consumer, "herobrinereturns" + "/kill_herobrine");

    }
}
