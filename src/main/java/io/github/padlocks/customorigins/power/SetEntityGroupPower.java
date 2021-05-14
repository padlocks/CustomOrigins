package io.github.padlocks.customorigins.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.player.PlayerEntity;

public class SetEntityGroupPower extends Power {
    public final EntityGroup group;

    public SetEntityGroupPower(PowerType<?> type, PlayerEntity player, EntityGroup group) {
        super(type, player);
        this.group = group;
    }
}
