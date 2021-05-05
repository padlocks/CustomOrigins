package io.github.padlocks.customorigins.impl.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.padlocks.customorigins.player.PlayerTicker;

import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(ClientPlayerEntity.class)
public abstract class HookClientPlayerTick extends AbstractClientPlayerEntity {
    protected HookClientPlayerTick(final ClientWorld world, final GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;tick(Z)V", ordinal = 0, shift = AFTER))
    private void afterInputTick(final CallbackInfo info) {
        final ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        PlayerTicker.get(player).afterInputTick(player);
    }
}
