package io.github.padlocks.customorigins.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    }
}
