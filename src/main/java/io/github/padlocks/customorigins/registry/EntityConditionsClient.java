package io.github.padlocks.customorigins.registry;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import io.github.padlocks.customorigins.CustomOriginsMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;

public class EntityConditionsClient {
    @SuppressWarnings("unchecked")
    @Environment(EnvType.CLIENT)
    public static void register() {
        register(new ConditionFactory<>(new Identifier(
                Origins.MODID, "block_looking_at"),
                new SerializableData().add("block_condition", SerializableDataType.BLOCK_CONDITION, null),
                (data, entity) -> {
                    ConditionFactory<CachedBlockPosition>.Instance blockCondition = (ConditionFactory<CachedBlockPosition>.Instance) data
                            .get("block_condition");
                    if (entity instanceof ServerPlayerEntity) {
                        ServerPlayerInteractionManager interactionManager = ((ServerPlayerEntity) entity).interactionManager;
                        double baseReach;
                        if (interactionManager.isCreative()) {
                            baseReach = 5.0D;
                        } else {
                            baseReach = 4.5D;
                        }
                        double reach = ReachEntityAttributes.getReachDistance(entity, baseReach);
                        Vec3d vec3d = entity.getCameraPosVec(0.0F);
                        Vec3d vec3d2 = entity.getRotationVec(0.0F);
                        Vec3d vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
                        BlockHitResult blockHitResult = entity.world.raycast(new RaycastContext(vec3d, vec3d3,
                                RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
                        if (blockHitResult != null && blockHitResult.getType() == HitResult.Type.BLOCK) {
                            return blockCondition
                                    .test(new CachedBlockPosition(entity.world, blockHitResult.getBlockPos(), true));
                        }
                    } else if (entity instanceof ClientPlayerEntity) {
                        ClientPlayerInteractionManager interactionManager = MinecraftClient
                                .getInstance().interactionManager;
                        double baseReach;
                        if (interactionManager.getCurrentGameMode().isCreative()) {
                            baseReach = 5.0D;
                        } else {
                            baseReach = 4.5D;
                        }
                        double reach = ReachEntityAttributes.getReachDistance(entity, baseReach);
                        Vec3d vec3d = entity.getCameraPosVec(0.0F);
                        Vec3d vec3d2 = entity.getRotationVec(0.0F);
                        Vec3d vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
                        BlockHitResult blockHitResult = entity.world.raycast(new RaycastContext(vec3d, vec3d3,
                                RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
                        if (blockHitResult != null && blockHitResult.getType() == HitResult.Type.BLOCK) {
                            return blockCondition
                                    .test(new CachedBlockPosition(entity.world, blockHitResult.getBlockPos(), true));
                        }
                    }
                    return false;
                }));
    }

    private static void register(ConditionFactory<LivingEntity> conditionFactory) {
        Registry.register(ModRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
