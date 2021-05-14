package io.github.padlocks.customorigins.power;

import java.util.LinkedHashMap;
import java.util.Map;

import io.github.apace100.origins.power.Active;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.HudRender;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import io.github.padlocks.customorigins.CustomOriginsMod;
import net.minecraft.entity.EntityGroup;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomOriginPowerFactories {
    private static final Map<PowerFactory<?>, Identifier> POWER_FACTORIES = new LinkedHashMap<>();
    public static final PowerFactory<Power> FACTORY_ENTITY_GROUP = create(
            new PowerFactory<>(new Identifier(CustomOriginsMod.MOD_ID, "entity_group"),
                    new SerializableData().add("group", SerializableDataType.ENTITY_GROUP),
                    data -> (type, player) -> new SetEntityGroupPower(type, player, (EntityGroup) data.get("group")))
                            .allowCondition());;
    public static final PowerFactory<Power> FACTORY_EXTRA_SOUL_SPEED = create(
            new PowerFactory<>(new Identifier(CustomOriginsMod.MOD_ID, "extra_soul_speed"),
                    new SerializableData().add("modifier", SerializableDataType.INT),
                    data -> (type, player) -> new ExtraSoulSpeedPower(type, player, data.getInt("modifier")))
                            .allowCondition());;
    public static final PowerFactory<Power> FACTORY_UNENCHANTED_SOUL_SPEED = create(
            new PowerFactory<>(new Identifier(CustomOriginsMod.MOD_ID, "unenchanted_soul_speed"),
                    new SerializableData().add("modifier", SerializableDataType.INT),
                    data -> (type, player) -> new UnenchantedSoulSpeedPower(type, player, data.getInt("modifier")))
                            .allowCondition());;
    public static final PowerFactory<Power> FACTORY_LIGHT_UP_BLOCK = create(
            new PowerFactory<>(new Identifier(CustomOriginsMod.MOD_ID, "light_up_block"),
                    new SerializableData().add("cooldown", SerializableDataType.INT)
                            .add("burn_time", SerializableDataType.INT, 1600)
                            .add("particle", SerializableDataType.PARTICLE_TYPE, ParticleTypes.FLAME)
                            .add("particle_count", SerializableDataType.INT, 15)
                            .add("sound", SerializableDataType.SOUND_EVENT, null)
                            .add("hud_render", SerializableDataType.HUD_RENDER)
                            .add("key", SerializableDataType.BACKWARDS_COMPATIBLE_KEY, new Active.Key()),
                    data -> (type, player) -> {
                        LightUpBlockPower power = new LightUpBlockPower(type, player, data.getInt("cooldown"),
                                (HudRender) data.get("hud_render"), data.getInt("burn_time"),
                                (ParticleType) data.get("particle"), data.getInt("particle_count"),
                                (SoundEvent) data.get("sound"));
                        power.setKey((Active.Key) data.get("key"));
                        return power;
                    })).allowCondition();;

    public static void register() {
        POWER_FACTORIES.keySet().forEach(
                powerType -> Registry.register(ModRegistries.POWER_FACTORY, POWER_FACTORIES.get(powerType), powerType));
    }

    private static <T extends Power> PowerFactory<T> create(PowerFactory<T> factory) {
        POWER_FACTORIES.put(factory, factory.getSerializerId());
        return factory;
    }

    /* private static void register(PowerFactory serializer) {
        Registry.register(ModRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    } */
}
