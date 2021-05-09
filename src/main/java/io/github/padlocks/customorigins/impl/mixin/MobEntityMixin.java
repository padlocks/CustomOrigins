package io.github.padlocks.customorigins.impl.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Mixin(MobEntity.class)
public class MobEntityMixin extends LivingEntity {
    private static final Logger LOGGER = LogManager.getLogger();
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    private ArrayList<ServerPlayerEntity> playersList = new ArrayList();

    private static final Predicate<LivingEntity> PLAYER_ENTITY_FILTER = (livingEntity) -> {
        if (livingEntity == null) {
            return false;
        } else if (livingEntity instanceof PlayerEntity) {
            return true;
        } else {
            return false;
        }
    };

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        LOGGER.info(player.getEntityName());
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Arm getMainArm() {
        // TODO Auto-generated method stub
        return null;
    }
}
