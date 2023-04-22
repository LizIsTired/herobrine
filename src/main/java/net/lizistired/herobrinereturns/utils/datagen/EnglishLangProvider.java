package net.lizistired.herobrinereturns.utils.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.lizistired.herobrinereturns.utils.registry.RegisterItems;
import net.minecraft.data.DataOutput;
import net.minecraft.util.Identifier;

import java.nio.file.Path;

class EnglishLangProvider extends FabricLanguageProvider {
    public EnglishLangProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(RegisterItems.CURSED_BOOK, "Cursed Book");
        translationBuilder.add("entity.minecraft.herobrinenametag", "/§kHerobrine");
        translationBuilder.add("entity.minecraft.herobrine", "/§kevVakMroxkwfamtsZKuIkzQJofkIQqIxDTwCQmFNmXzThTUnKeBkYGaJUoSnSxKHXILJrrmYxtzLskyRIvNermPsOVdPqPvMtJHlhfennmceacbbQnzysQsQnKFdoABRWyauMrSicwnaCfIItlViZiNNfMzaVgJZHWRMlreFLGlxBdCRNKbpTGBIXFZRrjboIRaJqwmwzbXkoQwluICoxTLOlParHBHaIkerCZPewSKluPgOhHIqTLIUqoYuLqRzFvXqUleNOvzMIyOjyqQjwQUEnSpALypjmobRixock");
        //translationBuilder.add(SIMPLE_ITEM_GROUP, "Simple Item Group");

        // Load an existing language file.
        try {
            Path existingFilePath = EnglishLangProvider.super.dataOutput.getModContainer().findPath("assets/herobninereturns/lang/en_us.existing.json").get();
            translationBuilder.add(existingFilePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add existing language file!", e);
        }
    }
}
