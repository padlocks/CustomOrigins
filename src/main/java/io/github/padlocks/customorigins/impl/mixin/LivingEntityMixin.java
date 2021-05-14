package io.github.padlocks.customorigins.impl.mixin;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.registry.ModComponents;
import io.github.padlocks.customorigins.CustomOriginsMod;
import io.github.padlocks.customorigins.event.LivingTickEvent;
import io.github.padlocks.customorigins.power.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
    public float changeSlipperiness$CustomOrigins(float t) {
        int level = EnchantmentHelper.getEquipmentLevel(CustomOriginsMod.GROUND_SPIKES, (LivingEntity) (Object) this);

        if (CustomOriginsPowers.SLIPPERY.isActive(this) && level == 0) {
            return 0.98f;
        } else {
            return t;
        }
    }

    @Inject(method = "shouldDisplaySoulSpeedEffects", at = @At("HEAD"), cancellable = true)
    private void shouldDisplaySoulSpeedEffects(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(
                this.age % 5 == 0 && this.getVelocity().x != 0.0D && this.getVelocity().z != 0.0D && !this.isSpectator()
                        && (EnchantmentHelper.hasSoulSpeed((LivingEntity) (Object) this)
                                || OriginComponent.hasPower(this, UnenchantedSoulSpeedPower.class)
                                || OriginComponent.hasPower(this, ExtraSoulSpeedPower.class))
                        && ((LivingEntityInvoker) this).invokeIsOnSoulSpeedBlock());
    }

    @ModifyVariable(method = "addSoulSpeedBoostIfNeeded", at = @At("STORE"), ordinal = 0)
    private int replaceLevelOfSouLSpeed(int i) {
        if (OriginComponent.hasPower(this, UnenchantedSoulSpeedPower.class) && i == 0) {
            return i = (OriginComponent.getPowers(this, UnenchantedSoulSpeedPower.class).get(0).getModifier());
        }
        if (OriginComponent.hasPower(this, ExtraSoulSpeedPower.class)) {
            return i += (OriginComponent.getPowers(this, ExtraSoulSpeedPower.class).get(0).getModifier());
        }
        return i;
    }

    @Inject(method = "addSoulSpeedBoostIfNeeded", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void itemStack(CallbackInfo ci, int i) {
        if (OriginComponent.hasPower(this, UnenchantedSoulSpeedPower.class)
                && i <= (OriginComponent.getPowers(this, UnenchantedSoulSpeedPower.class).get(0).getModifier())) {
            ci.cancel();
        }
        if (OriginComponent.hasPower(this, ExtraSoulSpeedPower.class)
                && i == (OriginComponent.getPowers(this, ExtraSoulSpeedPower.class).get(0).getModifier())) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "getGroup", cancellable = true)
    public void getGroup(CallbackInfoReturnable<EntityGroup> cir) {
        if ((Object) this instanceof PlayerEntity) {
            OriginComponent component = ModComponents.ORIGIN.get(this);
            List<SetEntityGroupPower> originsGroups = component.getPowers(SetEntityGroupPower.class);
            List<SetEntityGroupPower> tmoGroups = component.getPowers(SetEntityGroupPower.class);
            if (tmoGroups.size() > 0) {
                if (tmoGroups.size() > 1 || tmoGroups.size() > 0 && originsGroups.size() > 0) {
                    CustomOriginsMod.LOGGER.warn("Player " + this.getDisplayName().toString()
                            + " has two instances of SetEntityGroupPower.");
                }
                cir.setReturnValue(tmoGroups.get(0).group);
            }
        }
    }
}