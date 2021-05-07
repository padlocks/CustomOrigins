package io.github.padlocks.customorigins.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class HeadphonesEnchantment extends Enchantment {
    public HeadphonesEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[] { EquipmentSlot.HEAD });
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public int getMaxPower(int level) {
        return 30;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}