package io.github.padlocks.customorigins.event;

import java.util.Arrays;
import java.util.List;

import io.github.padlocks.customorigins.power.CustomOriginsPowers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import user11681.anvil.event.Listener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityEventListeners {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final EntityType<?>[] passiveEntities = {
        EntityType.PIG,
        EntityType.SHEEP,
        EntityType.COW,
        EntityType.CHICKEN,
        EntityType.VILLAGER,
        EntityType.HORSE,
        EntityType.LLAMA,
        EntityType.MOOSHROOM,
        EntityType.MULE,
        EntityType.DONKEY,
        EntityType.TRADER_LLAMA,
        EntityType.WANDERING_TRADER
    };
    public static final EntityType<?>[] hostileEntities = {
        EntityType.ZOMBIE,
        EntityType.DROWNED,
        EntityType.PHANTOM,
        EntityType.ZOMBIE_VILLAGER,
        EntityType.ZOMBIFIED_PIGLIN,
        EntityType.ZOGLIN,
        EntityType.SKELETON,
        EntityType.STRAY,
        EntityType.HUSK
    };

    @Listener
    public static void onLivingUpdate(final LivingTickEvent.Post event) {
        final LivingEntity entity = event.getEntity();
        if (Arrays.asList(passiveEntities).contains((EntityType<?>)entity.getType())) {
            MinecraftServer server = entity.getServer();
            if (server != null) {
                PlayerManager playerManager = server.getPlayerManager();
                List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
                playerList.forEach(p -> {
                    if (CustomOriginsPowers.UNDEAD.isActive(p)) {
                        boolean noticed = false;
                        if (!noticed) {
                            if (entity.isInRange(p, 15d)) {
                                entity.setAttacker(p);
                                entity.setYaw(p.getYaw(10.0F));
                                entity.setMovementSpeed(entity.getMovementSpeed() + 2f);
                                //entity.setGlowing(true);
                                noticed = true;
                            }
                        }

                        if (!entity.isInRange(p, 15d) && noticed) {
                            entity.setAttacker(null);
                            entity.setMovementSpeed(entity.getMovementSpeed() - 2f);
                            // entity.setGlowing(false);
                            noticed = false;
                        }
                        
                    }
                });
            } 
        }
/*         if (Arrays.asList(hostileEntities).contains((EntityType<?>) entity.getType())) {
            MinecraftServer server = entity.getServer();
            if (server != null) {
                PlayerManager playerManager = server.getPlayerManager();
                List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
                playerList.forEach(p -> {
                    if (CustomOriginsPowers.UNDEAD.isActive(p)) {
                        MobEntity monster = (MobEntity) event.getEntity();
                        if (monster.getTarget() != null && monster.getTarget().equals((LivingEntity) p)) {
                            monster.setTarget(null);
                            monster.setAttacking(null);
                            monster.setAttacking(false);
                        }
                    }
                });
            }
        } */
        if (entity.getType().equals(EntityType.IRON_GOLEM)) {
            MinecraftServer server = entity.getServer();
            if (server != null) {
                PlayerManager playerManager = server.getPlayerManager();
                List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
                playerList.forEach(p -> {
                    if (CustomOriginsPowers.UNDEAD.isActive(p)) {
                        IronGolemEntity golem = (IronGolemEntity) event.getEntity();
                        if (golem.isInRange(p, 25d)) {
                            golem.setAttacking((PlayerEntity) p);
                            golem.setAngryAt(p.getUuid());
                            golem.setAttacking(true);
                        }
                    }
                });
            }
        }
    }
}