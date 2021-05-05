package io.github.padlocks.customorigins.effect;

import io.github.padlocks.customorigins.CustomOriginsMod;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EffectRegistry {
    public static StatusEffect FLIGHT;
    public static void register() {
        FLIGHT = Registry.register(Registry.STATUS_EFFECT, new Identifier(CustomOriginsMod.MOD_ID, "flight"), new CustomStatusEffect(StatusEffectType.NEUTRAL, 0xFFFEE0));
    }

}