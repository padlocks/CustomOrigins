package io.github.padlocks.customorigins.power;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.networking.ModPackets;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.power.ActiveCooldownPower;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.registry.ModComponents;
import io.github.apace100.origins.util.HudRender;
import io.github.padlocks.customorigins.WorldUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SwitchyPower extends ActiveCooldownPower {

    public SwitchyPower(PowerType<?> type, PlayerEntity player, int cooldownDuration, HudRender hudRender) {
        super(type, player, cooldownDuration, hudRender, null);
    }

    public void onUse() {
        if (this.canUse()) {
            if (!player.world.isClient) {
                OriginComponent component = ModComponents.ORIGIN.get(player);
                OriginLayer base_layer = OriginLayers.getLayer(new Identifier(Origins.MODID, "origin"));
                OriginLayer class_layer = OriginLayers.getLayer(new Identifier("origins-classes", "class"));

                if (base_layer.isEnabled()) {
                    component.setOrigin(base_layer, Origin.EMPTY);
                }
                if (class_layer.isEnabled()) {
                    component.setOrigin(class_layer, Origin.EMPTY);
                }
                component.checkAutoChoosingLayers(player, false);
                component.sync();
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeBoolean(false);
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ModPackets.OPEN_ORIGIN_SCREEN, data);
            }

            this.use();
        }
    }
}
