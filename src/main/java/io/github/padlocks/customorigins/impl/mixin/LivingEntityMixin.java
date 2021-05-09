package io.github.padlocks.customorigins.impl.mixin;

import io.github.padlocks.customorigins.CustomOriginsMod;
import io.github.padlocks.customorigins.event.LivingTickEvent;
import io.github.padlocks.customorigins.power.CustomOriginsPowers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    protected void preTick(final CallbackInfo info) {
        if (new LivingTickEvent.Pre((LivingEntity) (Object) this).fire().isFail()) {
            info.cancel();
        }
    }

    @Inject(method = "tick()V", at = @At("RETURN"))
    protected void postTick(final CallbackInfo info) {
        new LivingTickEvent.Post((LivingEntity) (Object) this).fire();
    }

    // SLIPPERY
    @ModifyVariable(method = "travel", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    public float changeSlipperiness$MobOrigins(float t) {
        int level = EnchantmentHelper.getEquipmentLevel(CustomOriginsMod.GROUND_SPIKES, (LivingEntity) (Object) this);

        if (CustomOriginsPowers.SLIPPERY.isActive(this) && level == 0) {
            return 0.98f;
        } else {
            return t;
        }
    }
}