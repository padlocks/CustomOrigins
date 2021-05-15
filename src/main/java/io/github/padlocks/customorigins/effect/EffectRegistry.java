package io.github.padlocks.customorigins.effect;

import io.github.padlocks.customorigins.CustomOriginsMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EffectRegistry {
    public static StatusEffect FLIGHT;
    public static StatusEffect WITHER_RESISTANCE;
    public static StatusEffect CALMNESS;
    public static StatusEffect EVANESCENCE;
    
    public static void register() {
        FLIGHT = Registry.register(Registry.STATUS_EFFECT, new Identifier(CustomOriginsMod.MOD_ID, "flight"), 
                new CustomStatusEffect(StatusEffectType.NEUTRAL, 0xFFFEE0));
        WITHER_RESISTANCE = Registry.register(Registry.STATUS_EFFECT, new Identifier(CustomOriginsMod.MOD_ID, "wither_resistance"), 
                new WitherResistanceStatusEffect());
        CALMNESS = Registry.register(Registry.STATUS_EFFECT,
                new Identifier(CustomOriginsMod.MOD_ID, "calmness"), new CalmnessStatusEffect());
        EVANESCENCE = Registry.register(Registry.STATUS_EFFECT, new Identifier(CustomOriginsMod.MOD_ID, "evanescence"),
                new EvanescenceEffect(StatusEffectType.HARMFUL, 0));
    }
    
    public static EvanescenceEffect getEvanescenceEffect() {
            return (EvanescenceEffect) Registry.STATUS_EFFECT.get(
                            new Identifier(CustomOriginsMod.MOD_ID, "evanescence"));
    }

}