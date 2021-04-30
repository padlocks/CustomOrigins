package io.github.padlocks.customorigins.power;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.power.ModelColorPower;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomOriginPowerFactories {
    public static void register() {
    }


    private static void register(PowerFactory serializer) {
        Registry.register(ModRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
