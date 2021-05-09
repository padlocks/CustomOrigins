package io.github.padlocks.customorigins.client;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

import java.util.function.Function;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public String getModId() {
        return "customorigins";
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return parent -> {
            // load config
            Config.load();

            // create the config
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new TranslatableText("title.customorigins.config"));

            // config categories and entries
            ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.customorigins.general"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableText("option.customorigins.autoUpdate"), Config.isAutoUpdate())
                    .setTooltip(
                            new TranslatableText("option.tooltip.customorigins.autoUpdate"))
                    .setSaveConsumer(Config::setAutoUpdate)
                    .setDefaultValue(true)
                    .build());

            // build and return the config screen
            return builder.build();
        };
    }
}