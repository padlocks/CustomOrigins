package io.github.padlocks.customorigins.blocks;

import io.github.padlocks.customorigins.registry.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.StemBlock;

public class WitheredMelonBlock extends WitheredGourdBlock {

    public WitheredMelonBlock(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public StemBlock getStem() {
        return (WitheredStemBlock) ModBlocks.WITHERED_MELON_STEM;
    }

    @Environment(EnvType.CLIENT)
    public AttachedStemBlock getAttachedStem() {
        return (AttachedWitheredStemBlock) ModBlocks.ATTACHED_WITHERED_MELON_STEM;
    }
}
