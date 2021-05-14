package io.github.padlocks.customorigins.impl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.padlocks.customorigins.recipes.BrewingRecipeRegistrar;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;

@Mixin(BrewingRecipeRegistry.class)
public abstract class MixinBrewRegistry {

    @Shadow
    private static void registerPotionRecipe(Potion input, Item item, Potion output) {
    }

    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private static void afterRegistration(CallbackInfo cb) {
        BrewingRecipeRegistrar.onKeyReady((in, ing, out) -> registerPotionRecipe(in, ing, out));
    }

}
