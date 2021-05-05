package io.github.padlocks.customorigins;

import io.github.padlocks.customorigins.effect.EffectRegistry;
import io.github.padlocks.customorigins.power.CustomOriginPowerFactories;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class CustomOriginsMod implements ModInitializer {
    public static final String MOD_ID = "customorigins";

    public static Identifier id(String path) {
        return new Identifier("customorigins", path);
    }
    
    @Override
    public void onInitialize() {
        CustomOriginPowerFactories.register();
        EffectRegistry.register();
    }
}
