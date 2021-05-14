package io.github.padlocks.customorigins.potion;

import java.lang.reflect.Field;

import io.github.padlocks.customorigins.CustomOriginsMod;
import io.github.padlocks.customorigins.effect.EffectRegistry;
import net.minecraft.util.Identifier;

public class ModPotionRegistry {

    public static ModPotion calmness = ModPotion.ModPotionTimed.generateWithLengthened("calmness", EffectRegistry.CALMNESS, 20 * 60, 20 * 180);
    public static void registerAll() {
        try {
            int registered = 0;
            for (Field field : ModPotionRegistry.class.getDeclaredFields()) {
                if (ModPotion.class.isAssignableFrom(field.getType())) {
                    Identifier id = new Identifier(CustomOriginsMod.MOD_ID, field.getName());
                    CustomOriginsMod.LOGGER.debug("Registering potion " + id);
                    registered += ((ModPotion) field.get(null)).registerTree(CustomOriginsMod.MOD_ID, field.getName());
                }
            }

            CustomOriginsMod.LOGGER.info("Registered %d potions", registered);
        } catch (Exception e) {
            CustomOriginsMod.LOGGER.error(e);
        }
    }

}
