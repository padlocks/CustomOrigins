package io.github.padlocks.customorigins.power;

import io.github.apace100.origins.power.ActiveCooldownPower;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.util.HudRender;
import io.github.padlocks.customorigins.WorldUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public class HiveMindPower extends ActiveCooldownPower {

    public HiveMindPower(PowerType<?> type, PlayerEntity player, int cooldownDuration, HudRender hudRender) {
        super(type, player, cooldownDuration, hudRender, null);
    }

    public void onUse() {
        if (this.canUse()) {
            if (!player.world.isClient) {
                player.damage(DamageSource.CRAMMING, 4F);
                WorldUtil.spawnAngryBees(player.world, player.getPos());
            }
            
            this.use();
        }
    }
}
