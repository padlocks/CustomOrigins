package io.github.padlocks.customorigins.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Either;

import io.github.padlocks.customorigins.CustomOriginsMod;
import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.VanillaAbilities;
import io.github.padlocks.customorigins.effect.FlightEffect;
import io.github.padlocks.customorigins.misc.Command;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;


public class FlyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("fly");
        literal.requires(Command.IS_OP).executes(ctx -> execute(ctx,
                        Either.left(CustomOriginsMod.getRayTraceTarget(ctx.getSource().getEntityOrThrow(),
                                ctx.getSource().getWorld(), 160D, false, true))))
                .then(Command.argument("player", EntityArgumentType.player())
                        .executes(ctx -> execute(ctx, Either.right(EntityArgumentType.getPlayer(ctx, "player")))));
        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> ctx,
            Either<HitResult, ServerPlayerEntity> hitOrPlayer) {
        PlayerEntity player = (PlayerEntity) (hitOrPlayer.left().isPresent() ? hitOrPlayer.left().get()
                : hitOrPlayer.right().orElseThrow(NullPointerException::new));
        Pal.grantAbility(player, VanillaAbilities.ALLOW_FLYING,
                        FlightEffect.FLIGHT_POTION);
        return 1;
    }
}