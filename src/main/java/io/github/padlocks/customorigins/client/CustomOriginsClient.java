package io.github.padlocks.customorigins.client;

import io.github.padlocks.customorigins.entity.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class CustomOriginsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    }
}
