package io.github.padlocks.customorigins.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import net.minecraft.entity.player.PlayerEntity;

public class UnenchantedSoulSpeedPower extends Power {
    int modifier;

    public UnenchantedSoulSpeedPower(PowerType<?> type, PlayerEntity player, int modifier) {
        super(type, player);
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }
}
