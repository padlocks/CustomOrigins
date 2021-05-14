package io.github.padlocks.customorigins.networking;

import io.github.padlocks.customorigins.CustomOriginsMod;
import net.minecraft.util.Identifier;

public class NetworkingConstants {
    public static final Identifier UPDATE_CONFIG_PARTICLES = new Identifier(CustomOriginsMod.MOD_ID, "update_config_particles");
    public static final Identifier UPDATE_CONFIG_PLAYER_OVERLAYS = new Identifier(CustomOriginsMod.MOD_ID, "update_config_overlays");
    public static final Identifier SPAWN_PACKET = new Identifier(CustomOriginsMod.MOD_ID, "spawn_packet");
    public static final Identifier SPAWN = new Identifier(CustomOriginsMod.MOD_ID, "spawn");
}
