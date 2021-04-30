package io.github.padlocks.customorigins.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class CustomStatusEffect extends StatusEffect {
    protected CustomStatusEffect(StatusEffectType type, int color) {
        super(type, color);
    }
}
