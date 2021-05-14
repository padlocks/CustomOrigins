package io.github.padlocks.customorigins.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import io.github.padlocks.customorigins.networking.NetworkingConstants;

public class Config {
    public static final Path PROPERTIES_PATH = FabricLoader.getInstance().getConfigDir()
            .resolve("customorigins.properties");
    private static final Properties config = new Properties();
    private static boolean autoUpdate;
    private static boolean uniqueParticles;
    private static boolean displayGreetingScreen;
    private static boolean showPlayerOverlays;

    public static void load() {
        // if customorigins.properties exist, load it
        if (Files.isRegularFile(PROPERTIES_PATH)) {
            // load customorigins.properties
            try {
                config.load(Files.newBufferedReader(PROPERTIES_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // if no customorigins.properties, load default values
            // define default properties
            setAutoUpdate(true);
            setUniqueParticles(true);
            setDisplayGreetingScreen(true);
            setDisplayPlayerOverlays(true);
        }

        try {
            autoUpdate = Boolean.parseBoolean(config.getProperty("auto-update"));
            uniqueParticles = Boolean.parseBoolean(config.getProperty("unique-particles"));
        } catch (Exception e) {
            setAutoUpdate(true);
            setUniqueParticles(true);
            setDisplayGreetingScreen(true);
            setDisplayPlayerOverlays(true);
        }
    }

    public static void save() {
        try {
            config.store(Files.newBufferedWriter(Config.PROPERTIES_PATH), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAutoUpdate() {
        return autoUpdate;
    }

    public static boolean uniqueParticlesEnabled() {
        if (MinecraftClient.getInstance().player != null) {
            String uuid = MinecraftClient.getInstance().player.getUuidAsString();
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(uuid);
            buf.writeBoolean(uniqueParticles);
            ClientPlayNetworking.send(NetworkingConstants.UPDATE_CONFIG_PARTICLES, buf);
        }
        
        return uniqueParticles;
    }

    public static void setAutoUpdate(boolean value) {
        autoUpdate = value;
        config.setProperty("auto-update", Boolean.toString(value));
        Config.save();
    }

    public static void setUniqueParticles(boolean value) {
        autoUpdate = value;
        config.setProperty("unique-particles", Boolean.toString(value));
        Config.save();
        if (MinecraftClient.getInstance().player != null) {
            String uuid = MinecraftClient.getInstance().player.getUuidAsString();
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(uuid);
            buf.writeBoolean(uniqueParticles);
            ClientPlayNetworking.send(NetworkingConstants.UPDATE_CONFIG_PARTICLES, buf);
        }

    }

    public static boolean isDisplayGreetingScreen() {
        return displayGreetingScreen;
    }

    public static void setDisplayGreetingScreen(boolean value) {
        displayGreetingScreen = value;
        config.setProperty("display-greeting-screen", Boolean.toString(value));
        Config.save();
    }

    public static boolean isPlayerOverlaysEnabled() {
        if (MinecraftClient.getInstance().player != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(showPlayerOverlays);
            ClientPlayNetworking.send(NetworkingConstants.UPDATE_CONFIG_PLAYER_OVERLAYS, buf);
        }

        return showPlayerOverlays;
    }

    public static void setDisplayPlayerOverlays(boolean value) {
        showPlayerOverlays = value;
        config.setProperty("show-player-overlays", Boolean.toString(value));
        Config.save();
        if (MinecraftClient.getInstance().player != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(showPlayerOverlays);
            ClientPlayNetworking.send(NetworkingConstants.UPDATE_CONFIG_PLAYER_OVERLAYS, buf);
        }
    }

}
