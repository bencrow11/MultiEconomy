package com.bencrow11.multieconomy.command;

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.command.subcommand.AddBalanceCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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

				CommandUtils.registerAliases(dispatcher, root);

				root.addChild(new AddBalanceCommand().build());
	}

	public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		context.getSource().getPlayer().sendMessage(Text.literal("Base Command!"));
		return 1;
	}
}
