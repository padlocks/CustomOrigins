/*
 * PlayerAbilityLib
 * Copyright (C) 2019-2020 Ladysnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package io.github.padlocks.customorigins.impl;

import com.google.common.base.Preconditions;
import io.github.padlocks.customorigins.AbilitySource;
import io.github.padlocks.customorigins.AbilityTracker;
import io.github.padlocks.customorigins.PlayerAbility;
import io.github.padlocks.customorigins.PlayerAbilityUpdatedCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class PalInternals {

    public static final Logger LOGGER = LogManager.getLogger("PlayerAbilityLib");

    private static final Map<Identifier, PlayerAbility> abilities = new HashMap<>();
    private static final Map<Identifier, AbilitySource> sources = new HashMap<>();

    public static void populate(PlayerEntity player, Map<PlayerAbility, AbilityTracker> map) {
        for (PlayerAbility ability : abilities.values()) {
            map.put(ability, ability.createTracker(player));
        }
    }

    public static PlayerAbility getAbility(Identifier id) {
        Preconditions.checkNotNull(id);
        return abilities.get(id);
    }

    public static synchronized PlayerAbility registerAbility(PlayerAbility ability) {
        if (abilities.containsKey(ability.getId())) {
            throw new IllegalStateException("An ability was already registered with the id " + ability);
        }
        abilities.put(ability.getId(), ability);
        return ability;
    }

    public static AbilitySource registerSource(Identifier sourceId, Function<Identifier, AbilitySource> factory) {
        Preconditions.checkNotNull(sourceId);
        AbilitySource value = sources.get(sourceId);
        if (value == null) {
            synchronized (sources) {
                return sources.computeIfAbsent(sourceId, factory); // off-chance that someone modifies the map concurrently
            }
        }

        return value;
    }

    public static boolean isAbilityRegistered(Identifier abilityId) {
        return abilityId != null && abilities.containsKey(abilityId);
    }

    public static Event<PlayerAbilityUpdatedCallback> createUpdateEvent() {
        return EventFactory.createArrayBacked(PlayerAbilityUpdatedCallback.class,
            (listeners) -> (player, nowEnabled) -> {
                for (PlayerAbilityUpdatedCallback listener : listeners) {
                    listener.onAbilityUpdated(player, nowEnabled);
                }
            });
    }
}
