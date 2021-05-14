package io.github.padlocks.customorigins.recipes;

import io.github.padlocks.customorigins.potion.ModPotion;
import io.github.padlocks.customorigins.potion.ModPotionRegistry;
import io.github.padlocks.customorigins.recipes.BrewingRecipeRegistrar.Registar;
import io.github.padlocks.customorigins.utils.DelayedConsumer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;

public class BrewingRecipeRegistrar extends DelayedConsumer<Registar> {
    private static final BrewingRecipeRegistrar INSTANCE = new BrewingRecipeRegistrar();

    public static void init() {
        registerPotion(true, ModPotionRegistry.calmness, Items.SOUL_SAND, Potions.AWKWARD);

    }

    public static void registerPotion(boolean active, ModPotion potion, Item ingredient, Potion base) {
        if (active) {
            INSTANCE.consumeWhenReady(reg -> {
                reg.register(base, ingredient, potion);
                if (potion.getEmpowered() != null) {
                    reg.register(potion, Items.GLOWSTONE_DUST, potion.getEmpowered());
                }
                if (potion.getExtended() != null) {
                    reg.register(potion, Items.REDSTONE, potion.getExtended());
                }
            });
        }
    }

    public static void onKeyReady(Registar r) {
        INSTANCE.keyReady(r);
    }

    @FunctionalInterface
	public static interface Registar {
		void register(Potion input, Item item, Potion output);
	}
}
