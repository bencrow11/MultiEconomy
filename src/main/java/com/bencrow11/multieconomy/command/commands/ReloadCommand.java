package com.bencrow11.multieconomy.command.commands;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.command.CommandRegistry;
import com.bencrow11.multieconomy.command.SubCommandInterface;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.event.PlayerJoinHandler;
import com.bencrow11.multieconomy.permission.PermissionManager;
import com.bencrow11.multieconomy.util.Utils;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ReloadCommand implements SubCommandInterface {

	@Override
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("reload")
				.executes(this::run)
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.RELOAD_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission §b" +
						PermissionManager.RELOAD_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		context.getSource().sendMessage(Text.literal(Utils.formatMessage("§aReloading Config.",
				isPlayer)));

		ConfigManager.loadConfig(); // Loads the config from file.
		AccountManager.initialise(); // Adds saved accounts to memory.
		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinHandler()); // Registers PlayerJoin event handler.
		CommandRegistry.registerCommands(); // Registers the commands.
		Multieconomy.LOGGER.info("MultiEconomy reloaded.");
		ErrorManager.printErrorsToConsole();

		if (ErrorManager.getErrors().size() != 0) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cErrors occurred while reloading.",
					isPlayer)));
			if (isPlayer) {
				ErrorManager.printErrorsToPlayer(context.getSource().getPlayer());
			} else {
				ErrorManager.printErrorsToConsole();
			}
			return -1;
		}

		context.getSource().sendMessage(Text.literal(Utils.formatMessage("§aMultiEconomy reloaded successfully.",
				isPlayer)));
		return 1;
	}
}
