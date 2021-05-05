package io.github.padlocks.customorigins.player;

import net.minecraft.client.network.ClientPlayerEntity;
import java.lang.ref.WeakReference;

public final class PlayerTicker {
    private static PlayerTicker ticker = new PlayerTicker(null);

    public static PlayerTicker get(final ClientPlayerEntity player) {
        if (ticker.ref.get() != player) {
            ticker = new PlayerTicker(player);
        }

        return ticker;
    }

    private final WeakReference<ClientPlayerEntity> ref;

    private PlayerTicker(final ClientPlayerEntity player) {
        this.ref = new WeakReference<>(player);
    }

    public void afterInputTick(final ClientPlayerEntity player) {
        final float flightSpeed = FlightHelper.getFlightSpeed(player);
        player.abilities.setFlySpeed(flightSpeed);
    }

}