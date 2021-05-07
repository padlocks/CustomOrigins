package io.github.padlocks.customorigins.impl.mixin;

import com.mojang.authlib.GameProfile;

import io.github.apace100.origins.power.ModelColorPower;
import io.github.apace100.origins.power.PowerType;
import io.github.padlocks.customorigins.AbilityTracker;
import io.github.padlocks.customorigins.CustomOriginsMod;
import io.github.padlocks.customorigins.Pal;
import io.github.padlocks.customorigins.PlayerAbility;
import io.github.padlocks.customorigins.VanillaAbilities;
import io.github.padlocks.customorigins.effect.FlightEffect;
import io.github.padlocks.customorigins.impl.PalInternals;
import io.github.padlocks.customorigins.impl.PlayerAbilityView;
import io.github.padlocks.customorigins.impl.VanillaAbilityTracker;
import io.github.padlocks.customorigins.power.CustomOriginsPowers;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerAbilityView {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final SoundEvent[] SOUND_EVENTS = { SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, SoundEvents.PARTICLE_SOUL_ESCAPE };
    @Unique
    private final Map<PlayerAbility, AbilityTracker> palAbilities = new LinkedHashMap<>();

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(MinecraftServer server, ServerWorld world, GameProfile profile, ServerPlayerInteractionManager interactionManager, CallbackInfo ci) {
        PalInternals.populate(this, this.palAbilities);
    }

    @Override
    public Iterable<PlayerAbility> listPalAbilities() {
        return this.palAbilities.keySet();
    }

    @Override
    public AbilityTracker get(PlayerAbility abilityId) {
        return this.palAbilities.get(abilityId);
    }

    @Override
    public void refreshAllPalAbilities(boolean syncVanilla) {
        for (PlayerAbility ability : this.listPalAbilities()) {
            if (ability != VanillaAbilities.FLYING) {
                this.get(ability).refresh(false);
            }
        }
        if (syncVanilla) {
            this.sendAbilitiesUpdate();  // batch vanilla abilities updates
        }
    }

    @Inject(method = "sendAbilitiesUpdate", at = @At(value = "NEW", target = "net/minecraft/network/packet/s2c/play/PlayerAbilitiesS2CPacket"))
    private void checkAbilityConsistency(CallbackInfo ci) {
        for (PlayerAbility ability : this.listPalAbilities()) {
            AbilityTracker tracker = this.get(ability);
            if (tracker instanceof VanillaAbilityTracker && ability != VanillaAbilities.FLYING) { // flying is volatile anyway
                ((VanillaAbilityTracker) tracker).checkConflict();
            }
        }
    }

    @Inject(method = "writeCustomDataToTag", at = @At("RETURN"))
    private void writeAbilitiesToTag(CompoundTag tag, CallbackInfo ci) {
        ListTag list = new ListTag();
        for (Map.Entry<PlayerAbility, AbilityTracker> entry : this.palAbilities.entrySet()) {
            CompoundTag abilityTag = new CompoundTag();
            abilityTag.putString("ability_id", entry.getKey().getId().toString());
            entry.getValue().save(abilityTag);
            list.add(abilityTag);
        }
        tag.put("playerabilitylib:abilities", list);
    }

    @Inject(
        method = "readCustomDataFromTag",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER)
    )
    private void readAbilitiesFromTag(CompoundTag tag, CallbackInfo ci) {
        for (Tag t : tag.getList("playerabilitylib:abilities", NbtType.COMPOUND)) {
            CompoundTag abilityTag = ((CompoundTag) t);
            if (abilityTag.contains("ability_id")) {
                Identifier abilityId = Identifier.tryParse(abilityTag.getString("ability_id"));
                if (abilityId != null) {
                    AbilityTracker ability = this.palAbilities.get(PalInternals.getAbility(abilityId));
                    if (ability != null) {
                        ability.load(abilityTag);
                    }
                }
            }
        }
    }

    int statusTimer = 0;
    int soulAmbienceTimer = 0;
    SoundEvent[] ambienceChoices = SOUND_EVENTS;
    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // BUMBLE
        if (CustomOriginsPowers.BUMBLE.isActive(player)) {
            if (!world.isClient) {
                if (player instanceof PlayerEntity) {
                    Pal.grantAbility((PlayerEntity) (Object) this, VanillaAbilities.ALLOW_FLYING,
                            FlightEffect.FLIGHT_POTION);
                }
            }
        }

        // HAUNTED
        if (CustomOriginsPowers.HAUNTED.isActive(player)) {
            int level = EnchantmentHelper.getEquipmentLevel(CustomOriginsMod.HEADPHONES, (LivingEntity) player);
            if (!world.isClient && player instanceof PlayerEntity && level == 0) {
                if (soulAmbienceTimer >= ((Math.random() * (1200 - 80)) + 80)) {
                    soulAmbienceTimer = 0;
                    world.playSound(null, player.getBlockPos(), 
                            ambienceChoices[new Random().nextInt(ambienceChoices.length)], SoundCategory.AMBIENT, 1.5f, 1f);
                }
                soulAmbienceTimer++;
            }
        }

        return;
    }
}