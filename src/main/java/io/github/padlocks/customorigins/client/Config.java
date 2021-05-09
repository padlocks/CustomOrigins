package io.github.padlocks.customorigins.client;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    public static final Path PROPERTIES_PATH = FabricLoader.getInstance().getConfigDir()
            .resolve("customorigins.properties");
    private static final Properties config = new Properties();
    private static boolean autoUpdate;
    private static boolean displayGreetingScreen;

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
            setDisplayGreetingScreen(true);
        }

        try {
            autoUpdate = Boolean.parseBoolean(config.getProperty("auto-update"));
        } catch (Exception e) {
            setAutoUpdate(true);
            setDisplayGreetingScreen(true);
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

    public static void setAutoUpdate(boolean value) {
        autoUpdate = value;
        config.setProperty("auto-update", Boolean.toString(value));
        Config.save();
    }

    public static boolean isDisplayGreetingScreen() {
        return displayGreetingScreen;
    }

    public static void setDisplayGreetingScreen(boolean value) {
        displayGreetingScreen = value;
        config.setProperty("display-greeting-screen", Boolean.toString(value));
        Config.save();
    }

}
