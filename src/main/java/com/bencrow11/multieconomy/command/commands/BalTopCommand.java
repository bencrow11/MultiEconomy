package com.bencrow11.multieconomy.command.commands;

import com.bencrow11.multieconomy.account.Account;
import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.permission.PermissionManager;
import com.bencrow11.multieconomy.util.Utils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BalTopCommand {
	private static final int PAGE_SIZE = 5;

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
	                            CommandRegistryAccess commandRegistryAccess,
	                            CommandManager.RegistrationEnvironment registrationEnvironment) {

		LiteralCommandNode<ServerCommandSource> root = CommandManager
				.literal("balancetop")
				.executes(BalTopCommand::run)
				.then(CommandManager.argument("currency", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							CommandSource.suggestMatching(ConfigManager.getConfig().getCurrenciesAsString()
									, builder);
							return builder.buildFuture();
						})
						.executes(BalTopCommand::run)
						.then(CommandManager.argument("page", IntegerArgumentType.integer())
								.suggests((ctx, builder) -> {
									CommandSource.suggestMatching(new String[]{"1", "2", "3"}, builder);
									return builder.buildFuture();
								})
								.executes(BalTopCommand::run)))
				.build();

		dispatcher.getRoot().addChild(root);

		dispatcher.register(CommandManager.literal("baltop").redirect(root).executes(BalTopCommand::run));
	}

	public static int run(CommandContext<ServerCommandSource> context) {
		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.BALTOP_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission §b" +
						PermissionManager.BALTOP_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		int argLength = context.getInput().split(" ").length;

		Currency currency = null;
		int page = 1;

		if (argLength == 1) {
			currency = ConfigManager.getConfig().getCurrencyByName(ConfigManager.getConfig().getDefaultCurrency());
		}


		if (argLength == 2) {
			String currencyArg = StringArgumentType.getString(context, "currency");
			if (Utils.isStringInt(currencyArg)) {
				page = Integer.parseInt(currencyArg);

				currency = ConfigManager.getConfig().getCurrencyByName(ConfigManager.getConfig().getDefaultCurrency());
			} else {
				currency = ConfigManager.getConfig().getCurrencyByName(currencyArg);
			}
		}

		if (argLength == 3) {
			String currencyArg = StringArgumentType.getString(context, "currency");
			currency = ConfigManager.getConfig().getCurrencyByName(currencyArg);

			page = IntegerArgumentType.getInteger(context, "page");
		}

		if (currency == null) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cCurrency could not be found.",
					isPlayer)));
			return -1;
		}

		if (page < 1) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cPage number must be at least 1.",
					isPlayer)));
			return -1;
		}

		int index = (page - 1) * PAGE_SIZE;

		List<Account> balances = AccountManager.sortAccountsByBalance(currency).stream().distinct().collect(Collectors.toList());

		if (balances.size() <= index) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cPage " + page + " does not exist.",
					isPlayer)));
			return -1;
		}

		List<Account> pageBalances;

		if (balances.size() - 1 < PAGE_SIZE + index) {
			pageBalances = balances.subList(index, balances.size());
		} else {
			pageBalances = balances.subList(index, PAGE_SIZE);
		}

		int pages = (int) Math.ceil((double)balances.size() / (double) PAGE_SIZE);

		String output = "§7=== §eBalance Top §7(§b" + currency.getPlural() + "§7) ===\n\n";

		for (int i = 0; i < pageBalances.size(); i++) {
			Account account = pageBalances.get(i);
			output += "§7" + (index + i + 1)  + " - §b" + account.getUsername() + "§7: §f" + account.getBalance(currency) +
					"§a\n";
		}

		output += "§7Page " + page + "/" + pages;

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(output,
				isPlayer)));
		return 1;
	}

}
