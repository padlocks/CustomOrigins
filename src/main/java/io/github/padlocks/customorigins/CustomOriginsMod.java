package io.github.padlocks.customorigins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.padlocks.customorigins.commands.*;
import io.github.padlocks.customorigins.effect.EffectRegistry;
import io.github.padlocks.customorigins.enchantment.GroundSpikesEnchantment;
import io.github.padlocks.customorigins.enchantment.HeadphonesEnchantment;
import io.github.padlocks.customorigins.enchantment.HeatProtectionEnchantment;
import io.github.padlocks.customorigins.power.CustomOriginPowerFactories;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CustomOriginsMod implements ModInitializer {
    public static Formatting DF = Formatting.GOLD;
    public static Formatting SF = Formatting.YELLOW;
    public static Style DS = Style.EMPTY.withColor(DF);
    public static Style SS = Style.EMPTY.withColor(SF);
    
    public static final Logger log = LogManager.getLogger("CustomOrigins");
    public static final String MOD_ID = "customorigins";
    
    public static Enchantment HEADPHONES = new HeadphonesEnchantment();
    public static Enchantment HEAT_PROTECTION = new HeatProtectionEnchantment();
    public static Enchantment GROUND_SPIKES = new GroundSpikesEnchantment();

    public static Identifier id(String path) {
        return new Identifier("customorigins", path);
    }
    
    @Override
    public void onInitialize() {
        CustomOriginPowerFactories.register();
        EffectRegistry.register();
        Registry.register(Registry.ENCHANTMENT, new Identifier(MOD_ID, "headphones"), HEADPHONES);
        Registry.register(Registry.ENCHANTMENT, new Identifier(MOD_ID, "heat_protection"), HEAT_PROTECTION);
        Registry.register(Registry.ENCHANTMENT, new Identifier(MOD_ID, "ground_spikes"), GROUND_SPIKES);
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            SmiteCommand.register(dispatcher);
        });
    }

    // Blatantly copied from GameRenderer, can raytrace both entities and blocks.
    public static HitResult getRayTraceTarget(Entity entity, World world, double reach, boolean ignoreEntities,
            boolean ignoreLiquids) {
        HitResult crosshairTarget = null;
        if (entity != null && world != null) {
            float td = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
                    ? MinecraftClient.getInstance().getTickDelta()
                    : 1f;
            crosshairTarget = entity.raycast(reach, td, !ignoreLiquids);
            if (!ignoreEntities) {
                Vec3d vec3d = entity.getCameraPosVec(td);
                double e = reach;
                e *= e;
                if (crosshairTarget != null)
                    e = crosshairTarget.getPos().squaredDistanceTo(vec3d);
                Vec3d vec3d2 = entity.getRotationVec(td);
                Vec3d vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
                Box box = entity.getBoundingBox().stretch(vec3d2.multiply(reach)).expand(1.0D, 1.0D, 1.0D);
                EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, vec3d, vec3d3, box,
                        (entityx) -> !entityx.isSpectator() && entityx.collides(), e);
                if (entityHitResult != null)
                    crosshairTarget = entityHitResult;
            }
        }
        return crosshairTarget;
    }
}
