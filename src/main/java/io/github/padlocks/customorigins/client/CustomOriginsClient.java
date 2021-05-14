package io.github.padlocks.customorigins.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.padlocks.customorigins.blocks.WitheredStemBlock;
import io.github.padlocks.customorigins.networking.EntityDispatcher;
import io.github.padlocks.customorigins.networking.NetworkingConstants;
import io.github.padlocks.customorigins.networking.packet.EntitySpawnPacket;
import io.github.padlocks.customorigins.registry.EntityConditionsClient;
import io.github.padlocks.customorigins.registry.ModBlocks;
import io.github.padlocks.customorigins.updater.CustomOriginsUpdater;

@Environment(EnvType.CLIENT)
public class CustomOriginsClient implements ClientModInitializer {
    public static final Logger logger = LogManager.getLogger("Customorigins");
    public static final String MOD_ID = "customorigins";

    @Override
    public void onInitializeClient() {
        Config.load();

        // auto-updater
        if (!FabricLoader.getInstance().isDevelopmentEnvironment() && Config.isAutoUpdate()) {
            CustomOriginsUpdater.init();
        }

        EntityConditionsClient.register();

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHERED_BEETROOTS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHERED_CARROTS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ATTACHED_WITHERED_MELON_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHERED_MELON_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHERED_POTATOES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ATTACHED_WITHERED_PUMPKIN_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHERED_PUMPKIN_STEM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHERED_WHEAT, RenderLayer.getCutout());

        ClientSidePacketRegistry.INSTANCE.register(NetworkingConstants.SPAWN, EntityDispatcher::spawnFrom);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            int j = 7 * 32;
            int k = 255 - 7 * 8;
            int l = 7 * 14;
            return j << 16 | k << 8 | l;
        }, ModBlocks.ATTACHED_WITHERED_PUMPKIN_STEM, ModBlocks.ATTACHED_WITHERED_MELON_STEM);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            int i = state.get(WitheredStemBlock.AGE);
            int j = i * 32;
            int k = 255 - i * 8;
            int l = i * 14;
            return j << 16 | k << 8 | l;
        }, ModBlocks.WITHERED_PUMPKIN_STEM, ModBlocks.WITHERED_MELON_STEM);
    }

    @Environment(EnvType.CLIENT)
    public void receiveEntityPacket() {
        ClientSidePacketRegistry.INSTANCE.register(NetworkingConstants.SPAWN_PACKET, (ctx, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(MinecraftClient.getInstance().world);
                if (e == null)
                    throw new IllegalStateException(
                            "Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                e.updateTrackedPosition(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.pitch = pitch;
                e.yaw = yaw;
                e.setEntityId(entityId);
                e.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
        });
    }
}
