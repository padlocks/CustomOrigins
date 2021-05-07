package io.github.padlocks.customorigins;

import io.github.padlocks.customorigins.effect.EffectRegistry;
import io.github.padlocks.customorigins.enchantment.GroundSpikesEnchantment;
import io.github.padlocks.customorigins.enchantment.HeadphonesEnchantment;
import io.github.padlocks.customorigins.enchantment.HeatProtectionEnchantment;
import io.github.padlocks.customorigins.power.CustomOriginPowerFactories;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;

public class CustomOriginsMod implements ModInitializer {
    public static final String MOD_ID = "customorigins";
    public static Enchantment HEADPHONES = new HeadphonesEnchantment();
    public static Enchantment HEAT_PROTECTION = new HeatProtectionEnchantment();
    public static Enchantment GROUND_SPIKES = new GroundSpikesEnchantment();

    public static Identifier id(String path) {
        return new Identifier("customorigins", path);
    }
    
    @Override
    public void onInitialize() {
        CustomOriginPowerFactories.register();
        EffectRegistry.register();
        Registry.register(Registry.ENCHANTMENT, new Identifier(MOD_ID, "headphones"), HEADPHONES);
        Registry.register(Registry.ENCHANTMENT, new Identifier(MOD_ID, "heat_protection"), HEAT_PROTECTION);
        Registry.register(Registry.ENCHANTMENT, new Identifier(MOD_ID, "ground_spikes"), GROUND_SPIKES);
    }
}
