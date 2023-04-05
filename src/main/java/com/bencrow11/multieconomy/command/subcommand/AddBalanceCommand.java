package com.bencrow11.multieconomy.command.subcommand;

import com.bencrow11.multieconomy.config.ConfigManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class AddBalanceCommand implements SubCommand {

	@Override
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("add")
				.then(CommandManager.argument("player", EntityArgumentType.player())
						.then(CommandManager.argument("currency", StringArgumentType.string())
								.suggests((ctx, builder) -> {
									CommandSource.suggestMatching(ConfigManager.getConfig().getCurrenciesAsString()
											, builder);
									return builder.buildFuture();
								})
								.then(CommandManager.argument("amount", IntegerArgumentType.integer())
										.executes(this::run))))
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		context.getSource().getPlayer().sendMessage(Text.literal("Add Balance Command!"));
		return 1;
	}
}
