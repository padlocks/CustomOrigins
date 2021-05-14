package io.github.padlocks.customorigins.impl.mixin;

import io.github.padlocks.customorigins.power.CustomOriginsPowers;
import io.github.padlocks.customorigins.registry.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock implements ItemConvertible {

    public BlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onPlaced", at = @At("HEAD"), cancellable = true)
    private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack,
            CallbackInfo ci) {
        if (placer != null && CustomOriginsPowers.BLACK_THUMB.isActive(placer)) {
            if ((Block) (Object) this == Blocks.WHEAT) {
                world.setBlockState(pos, ModBlocks.WITHERED_WHEAT.getDefaultState());
            } else if ((Block) (Object) this == Blocks.POTATOES) {
                world.setBlockState(pos, ModBlocks.WITHERED_POTATOES.getDefaultState());
            } else if ((Block) (Object) this == Blocks.CARROTS) {
                world.setBlockState(pos, ModBlocks.WITHERED_CARROTS.getDefaultState());
            } else if ((Block) (Object) this == Blocks.BEETROOTS) {
                world.setBlockState(pos, ModBlocks.WITHERED_BEETROOTS.getDefaultState());
            } else if ((Block) (Object) this == Blocks.MELON_STEM) {
                world.setBlockState(pos, ModBlocks.WITHERED_MELON_STEM.getDefaultState());
            } else if ((Block) (Object) this == Blocks.PUMPKIN_STEM) {
                world.setBlockState(pos, ModBlocks.WITHERED_PUMPKIN_STEM.getDefaultState());
            }
        }
    }
}
