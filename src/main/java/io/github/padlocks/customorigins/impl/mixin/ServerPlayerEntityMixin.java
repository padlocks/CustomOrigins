package io.github.padlocks.customorigins.impl.mixin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import com.mojang.authlib.GameProfile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.padlocks.customorigins.AbilityTracker;
import io.github.padlocks.customorigins.CustomOriginsMod;
import io.github.padlocks.customorigins.Pal;
import io.github.padlocks.customorigins.PlayerAbility;
import io.github.padlocks.customorigins.VanillaAbilities;
import io.github.padlocks.customorigins.WorldUtil;
import io.github.padlocks.customorigins.effect.FlightEffect;
import io.github.padlocks.customorigins.impl.PalInternals;
import io.github.padlocks.customorigins.impl.PlayerAbilityView;
import io.github.padlocks.customorigins.impl.VanillaAbilityTracker;
import io.github.padlocks.customorigins.networking.NetworkingConstants;
import io.github.padlocks.customorigins.power.CustomOriginsPowers;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
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
    boolean givenEffects = false;
    boolean enableParticles = false;
    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        String uuid = player.getUuidAsString();

        // Player Stuff
        if (uuid.equals("ad42478f-8de1-41de-bc59-19313e27cbda")) {
            ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.UPDATE_CONFIG_PARTICLES,
                    (server, serverPlayer, handler, buf, responseSender) -> {
                        // Read packet data on the event loop
                        String playerUuid = buf.readString(32767);
                        Boolean clientParticles = buf.readBoolean();

                        server.execute(() -> {
                            if (uuid.equals(playerUuid)) {
                                enableParticles = !clientParticles;
                                if (!world.isClient) {
                                    /* if (!givenEffects) {
                                        player.addExperienceLevels(2);
                                        givenEffects = true;
                                    } */
                                    
                                    if (enableParticles && !player.isInvisible()) {
                                        ((ServerWorld) player.world).spawnParticles((new DustParticleEffect(
                                            1.0f, 0.0f, 0.0f, 0.6f)),
                                            player.getX(),
                                            player.getRandomBodyY(), player.getZ(), 1, 
                                                ((Math.random() * (0.2d - 0.1d)) + 0.1d), 0.0D,
                                                ((Math.random() * (0.2d - 0.1d)) + 0.1d), 0.01f);
                                    }
                                }
                            }
                        });
            });
        }

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
                    player.playSound(ambienceChoices[new Random().nextInt(ambienceChoices.length)], SoundCategory.AMBIENT, 1.5f, 1f);
                }
                soulAmbienceTimer++;
            }
        }

        return;
    }
}