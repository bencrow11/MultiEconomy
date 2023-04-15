package com.bencrow11.multieconomy.command;

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.command.commands.*;
import com.bencrow11.multieconomy.util.Utils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public abstract class BaseCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
	                            CommandRegistryAccess commandRegistryAccess,
	                            CommandManager.RegistrationEnvironment registrationEnvironment) {

		LiteralCommandNode<ServerCommandSource> root = CommandManager
				.literal(Multieconomy.BASE_COMMAND)
				.executes(BaseCommand::run)
				.build();

		dispatcher.getRoot().addChild(root);


		for (String alias : Multieconomy.ALIASES) {
			dispatcher.register(CommandManager.literal(alias).redirect(root).executes(BaseCommand::run));
		}

		root.addChild(new AddBalanceCommand().build());
		root.addChild(new RemoveBalanceCommand().build());
		root.addChild(new SetBalanceCommand().build());
		root.addChild(new ClearBalanceCommand().build());
		root.addChild(new HelpCommand().build());
	}

	public static int run(CommandContext<ServerCommandSource> context) {
		context.getSource().sendMessage(Text.literal("§aRunning §bMultiEconomy §b" + Multieconomy.VERSION +
				"§a."));
		return 1;
	}
}
