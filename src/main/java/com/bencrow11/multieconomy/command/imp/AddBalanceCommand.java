package com.bencrow11.multieconomy.command.imp;

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public abstract class AddBalanceCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
		dispatcher.register(CommandManager.literal(Multieconomy.BASE_COMMAND)
				.then(CommandManager.literal("add")
						.then(CommandManager.argument("player", EntityArgumentType.player())
						.then(CommandManager.argument("currency", StringArgumentType.string())
								.suggests((ctx, builder) -> {
									CommandSource.suggestMatching(ConfigManager.getConfig().getCurrenciesAsString()
											, builder);
									return builder.buildFuture();
								})
						.then(CommandManager.argument("amount", IntegerArgumentType.integer())
								.executes(AddBalanceCommand::run))))));
	}

	public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		context.getSource().getPlayer().sendMessage(Text.literal("Command ran successfully!"));
		return 1;
	}
}
