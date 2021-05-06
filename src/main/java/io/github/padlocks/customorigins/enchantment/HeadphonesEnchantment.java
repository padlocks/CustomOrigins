package io.github.padlocks.customorigins.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class HeadphonesEnchantment extends Enchantment {
    public HeadphonesEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.ARMOR, new EquipmentSlot[] { EquipmentSlot.HEAD });
    }

    @Override
    public int getMinPower(int level) {
        return 7 + level * 7;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 7;
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
