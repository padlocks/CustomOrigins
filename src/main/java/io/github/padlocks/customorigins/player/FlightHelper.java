package io.github.padlocks.customorigins.player;

import io.github.padlocks.customorigins.power.CustomOriginsPowers;
import net.minecraft.client.network.ClientPlayerEntity;

final class FlightHelper {
    private FlightHelper() {
    }

    private static final float BASE_FLIGHT_SPEED = 0.05F;
    private static final float BUMBLE_FLIGHT_SPEED = 0.0155F;

    static float getFlightSpeed(final ClientPlayerEntity player) {
        if (!player.isCreative() && CustomOriginsPowers.BUMBLE.isActive(player)) {
            return BUMBLE_FLIGHT_SPEED;
        }
        else
            return BASE_FLIGHT_SPEED;
    }
}