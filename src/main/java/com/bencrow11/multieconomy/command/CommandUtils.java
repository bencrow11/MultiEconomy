package com.bencrow11.multieconomy.command;

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.command.imp.AddBalanceCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public abstract class CommandUtils {
	public static void registerAliases(CommandDispatcher<ServerCommandSource> dispatcher,
	                                   LiteralCommandNode<ServerCommandSource> command) {
		for (String alias : Multieconomy.ALIASES) {
			dispatcher.register(CommandManager.literal(alias).redirect(command));
		}
	}

	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register(AddBalanceCommand::register);
	}
}
