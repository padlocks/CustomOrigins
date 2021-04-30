package io.github.padlocks.customorigins;

import io.github.padlocks.customorigins.effect.EffectRegistry;
import io.github.padlocks.customorigins.entity.EntityRegistry;
import io.github.padlocks.customorigins.power.CustomOriginPowerFactories;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomOriginsMod implements ModInitializer {
    public static final String MOD_ID = "customorigins";

    @Override
    public void onInitialize() {
        CustomOriginPowerFactories.register();
        EffectRegistry.register();

    }
}
