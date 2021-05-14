package io.github.padlocks.customorigins.impl.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.padlocks.customorigins.power.CustomOriginsPowers;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {

    @Inject(method = "angerBees", at = @At("HEAD"), cancellable = true)
    public void angerBees$CustomOrigins(@Nullable PlayerEntity player, BlockState state,
            BeehiveBlockEntity.BeeState beeState, CallbackInfo ci) {
        if (CustomOriginsPowers.QUEEN_BEE.isActive(player)) {
            ci.cancel();
            return;
        }
    }
}
