package io.github.padlocks.customorigins.impl.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityInvoker {
    @Invoker("isOnSoulSpeedBlock")
    boolean invokeIsOnSoulSpeedBlock();
}
