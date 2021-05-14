package io.github.padlocks.customorigins.impl.mixin;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import io.github.padlocks.customorigins.power.CustomOriginsPowers;

import java.util.List;

@Mixin(BeehiveBlock.class)
public class BeehiveBlockMixin {

    @ModifyVariable(method = "angerNearbyBees", at = @At("STORE"), index = 4)
    public List<PlayerEntity> angerNearbyBees$CustomOrigins(List<PlayerEntity> list) {
        list.removeIf(CustomOriginsPowers.QUEEN_BEE::isActive);
        return list;
    }
}
