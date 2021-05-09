package io.github.padlocks.customorigins.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Either;
import io.github.padlocks.customorigins.CustomOriginsMod;
import io.github.padlocks.customorigins.misc.Command;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;


public class SmiteCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("smite");
        literal.requires(Command.IS_OP).executes(ctx -> execute(ctx,
                        Either.left(CustomOriginsMod.getRayTraceTarget(ctx.getSource().getEntityOrThrow(),
                                ctx.getSource().getWorld(), 160D, false, true))))
                .then(Command.argument("player", EntityArgumentType.player())
                        .executes(ctx -> execute(ctx, Either.right(EntityArgumentType.getPlayer(ctx, "player")))));
        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> ctx,
            Either<HitResult, ServerPlayerEntity> hitOrPlayer) {
        Vec3d pos = hitOrPlayer.left().isPresent() ? hitOrPlayer.left().get().getPos()
                : hitOrPlayer.right().orElseThrow(NullPointerException::new).getPos();
        for (int i = 0; i < 3; i++) {
            LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, ctx.getSource().getWorld());
            bolt.setPos(pos.getX(), pos.getY(), pos.getZ());
            ctx.getSource().getWorld().spawnEntity(bolt);
        }
        return 1;
    }
}