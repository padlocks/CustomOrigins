package io.github.padlocks.customorigins.impl.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class HookPlayerFlightSpeed extends LivingEntity {
    protected HookPlayerFlightSpeed(final EntityType<? extends LivingEntity> type, final World world) {
        super(type, world);
    }
}