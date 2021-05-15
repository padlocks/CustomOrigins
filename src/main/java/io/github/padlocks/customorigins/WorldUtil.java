package io.github.padlocks.customorigins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

import io.github.padlocks.customorigins.effect.EffectRegistry;
import io.github.padlocks.customorigins.entity.AttackThingsThatAreNotBeesGoal;
import io.github.padlocks.customorigins.entity.MobEntityTargetSelectorAccessor;
import io.github.padlocks.customorigins.power.CustomOriginsPowers;

public class WorldUtil {
    public static void spawnAngryBees(World world, Vec3d vec) {
        Box targetBox = new Box(vec, vec).expand(CustomOriginsMod.BEE_SEARCH_RADIUS);

        Optional<LivingEntity> foundTarget = world
                .getEntitiesByClass(LivingEntity.class, targetBox, WorldUtil::isValidBeeTarget).stream()
                .reduce((entityA, entityB) -> entityB.squaredDistanceTo(vec) < entityA.squaredDistanceTo(vec) ? entityB
                        : entityA);

        int bees = 3 + world.random.nextInt(5) + world.random.nextInt(5);

        int maxTime = 3000;
        int ticksToExist = maxTime / bees;

        for (int i = 0; i < bees; i++) {
            BlockPos spawnPos = new BlockPos(vec.x, vec.y, vec.z);
            BeeEntity bee = EntityType.BEE.spawn((ServerWorld) world, null, null, null, spawnPos, SpawnReason.EVENT, false, false);
            bee.setPos(vec.x, vec.y, vec.z);
            bee.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, maxTime, 1, false, false));
            bee.addStatusEffect(
                    new StatusEffectInstance(EffectRegistry.getEvanescenceEffect(), ticksToExist, 0, false, false));
            foundTarget.ifPresent(target -> { // make bee angry at target
                if (!CustomOriginsPowers.HIVE_MIND.isActive(target)) {
                    bee.setAngryAt(target.getUuid());
                    bee.setAngerTime(1200);
                    ((MobEntityTargetSelectorAccessor) bee).getTargetSelector().add(0,
                            new AttackThingsThatAreNotBeesGoal(bee));
                }
                else {
                    bee.setAngryAt(null);
                    bee.setAngerTime(0);
                }
            });
        }
    }

    public static boolean isValidBeeTarget(LivingEntity ent) {
        return (ent.getType() != EntityType.BEE) && (!ent.isInvulnerable()) && (!CustomOriginsPowers.HIVE_MIND
                .isActive(ent));
    }
}
