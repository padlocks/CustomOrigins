package io.github.padlocks.customorigins.impl.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.padlocks.customorigins.CustomOriginsMod;
import io.github.padlocks.customorigins.power.CustomOriginsPowers;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity {

    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void aerialAffinity(BlockState block, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        CustomOriginsMod.LOGGER.info(player.abilities.flying + " " + player.isOnGround());
        if (!this.onGround && CustomOriginsPowers.AERIAL_AFFINITY.isActive(this))
            cir.setReturnValue(cir.getReturnValue() * 5);
    }

    public PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

}