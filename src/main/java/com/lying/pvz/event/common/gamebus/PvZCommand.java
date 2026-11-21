package com.lying.pvz.event.common.gamebus;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.lying.pvz.PvZMod;
import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.event.invasion.InvasionEndEvent;
import com.lying.pvz.api.invasion.IInvasionType;
import com.lying.pvz.common.invasion.Invasion;
import com.lying.pvz.common.invasion.InvasionManager;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.server.players.NameAndId;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;


import java.util.Collection;

/**
 * {@code pvz} 调试命令。
 *
 * <p>格式一：{@code pvz <玩家UUID/名称> <资源> <add|remove> <num>}（仅在线玩家）
 * <p>格式二：{@code pvz invasion <入侵ID> <开始tick>}（延迟 tick 后触发，目标为命令执行者）
 * <p>格式三：{@code pvz invasion cancel}（取消当前正在进行的入侵）
 */
@EventBusSubscriber(modid = PvZMod.MOD_ID)
public class PvZCommand {
    private PvZCommand() {}

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var root = Commands.literal("pvz")
                .requires(src -> src.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER));

        // 格式一：pvz <player> <resource> <add|remove> <num>
        var playerArg = Commands.argument("player", GameProfileArgument.gameProfile());
        registerResource(playerArg, "tree_level", Resources.TREE_LVL);
        registerResource(playerArg, "sun_number", Resources.SUN_NUM);
        registerResource(playerArg, "money", Resources.MONEY);
        registerResource(playerArg, "max_energy_num", Resources.MAX_ENERGY_NUM);
        registerResource(playerArg, "gem_num", Resources.GEM_NUM);
        root.then(playerArg);

        // 格式二 / 三：pvz invasion <id> <tick> | pvz invasion cancel
        root.then(Commands.literal("invasion")
                .then(Commands.literal("cancel").executes(PvZCommand::cancelInvasion))
                .then(Commands.argument("invasion", IdentifierArgument.id())
                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggestResource(
                                PVZAPI.invasion().invasionTypes().keySet(), builder))
                        .then(Commands.argument("tick", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                .executes(PvZCommand::scheduleInvasion))));

        dispatcher.register(root);
    }

    private static void registerResource(ArgumentBuilder<CommandSourceStack, ?> playerArg, String key, Resources res) {
        playerArg.then(Commands.literal(key)
                .then(Commands.literal("add")
                        .then(Commands.argument("num", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                .executes(ctx -> modifyResource(ctx, res, false))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("num", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                .executes(ctx -> modifyResource(ctx, res, true)))));
    }

    private static int modifyResource(CommandContext<CommandSourceStack> ctx, Resources res, boolean remove)
            throws CommandSyntaxException {
        int num = IntegerArgumentType.getInteger(ctx, "num");
        if (remove) num = -num;
        ServerPlayer target = resolvePlayer(ctx);
        PlayerUtil.addResource(target, res, num);
        int now = PlayerUtil.getResource(target, res);
        ctx.getSource().sendSuccess(
                () -> Component.translatable("command.pvz.resource_modified",
                        target.getGameProfile().name(), res.getText(), now), true);
        return now;
    }

    private static int scheduleInvasion(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer target = source.getPlayerOrException();
        Identifier id = IdentifierArgument.getId(ctx, "invasion");
        int tick = IntegerArgumentType.getInteger(ctx, "tick");

        IInvasionType type = PVZAPI.invasion().invasionTypes().get(id);
        if (type == null) {
            source.sendFailure(Component.translatable("command.pvz.unknown_invasion", id.toString()));
            return 0;
        }
        if (InvasionManager.hasActive(target)) {
            source.sendFailure(Component.translatable("command.pvz.invasion_active"));
            return 0;
        }

        InvasionManager.scheduleInvasion(target, type, tick);
        source.sendSuccess(
                () -> Component.translatable("command.pvz.invasion_scheduled", id.toString(), tick), false);
        return 1;
    }

    private static int cancelInvasion(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer target = source.getPlayerOrException();
        Invasion inv = InvasionManager.getActive(target);
        if (inv == null || !inv.isRunning()) {
            source.sendFailure(Component.translatable("command.pvz.no_invasion"));
            return 0;
        }
        inv.disableWith(InvasionEndEvent.Reason.COMMAND);
        source.sendSuccess(() -> Component.translatable("command.pvz.invasion_cancelled"), false);
        return 1;
    }

    private static ServerPlayer resolvePlayer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<NameAndId> profiles = GameProfileArgument.getGameProfiles(ctx, "player");
        NameAndId profile = profiles.iterator().next();
        ServerPlayer player = ctx.getSource().getServer().getPlayerList().getPlayer(profile.id());
        if (player == null) {
            throw new SimpleCommandExceptionType(
                    Component.translatable("command.pvz.player_offline", profile.name())).create();
        }
        return player;
    }
}
