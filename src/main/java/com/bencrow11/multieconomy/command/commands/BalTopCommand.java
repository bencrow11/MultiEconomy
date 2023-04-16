/**
 * This file is part of MultiEconomy.
 *
 * MultiEconomy is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * MultiEconomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
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

/**
 * Creates the command "/baltop [page|currency] [page]" in game.
 */
public abstract class BalTopCommand {
	private static final int PAGE_SIZE = 5; // Amount of players per page

	/**
	 * Method to register and build the command.
	 */
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

		// Adds alias baltop to balancetop.
		dispatcher.register(CommandManager.literal("baltop").redirect(root).executes(BalTopCommand::run));
	}

	/**
	 * Method that's used to execute the functionality for the command.
	 * @param context the source of the command.
	 * @return integer to complete the command.
	 */
	public static int run(CommandContext<ServerCommandSource> context) {
		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		// If the source is a player, check for permission.
		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.BALTOP_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission §b" +
						PermissionManager.BALTOP_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		// Counts the amount of arguments given.
		int argLength = context.getInput().split(" ").length;

		Currency currency = null;
		int page = 1; // Sets the page to 1 by default.

		// If there is only one argument (baltop) get the default currency.
		if (argLength == 1) {
			currency = ConfigManager.getConfig().getCurrencyByName(ConfigManager.getConfig().getDefaultCurrency());
		}

		// If there are two arguments, check for a page number or currency. Set the currency to the one given
		// or use the default, if a page number is given.
		if (argLength == 2) {
			String currencyArg = StringArgumentType.getString(context, "currency");
			if (Utils.isStringInt(currencyArg)) {
				page = Integer.parseInt(currencyArg);

				currency = ConfigManager.getConfig().getCurrencyByName(ConfigManager.getConfig().getDefaultCurrency());
			} else {
				currency = ConfigManager.getConfig().getCurrencyByName(currencyArg);
			}
		}

		// If there are three arguments, get the currency given and the page number.
		if (argLength == 3) {
			String currencyArg = StringArgumentType.getString(context, "currency");
			currency = ConfigManager.getConfig().getCurrencyByName(currencyArg);

			page = IntegerArgumentType.getInteger(context, "page");
		}

		// If no currency was found, tell the sender.
		if (currency == null) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cCurrency could not be found.",
					isPlayer)));
			return -1;
		}

		// If the page number is less than 1, tell the user this is invalid.
		if (page < 1) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cPage number must be at least 1.",
					isPlayer)));
			return -1;
		}

		// Set the index based on the page requested.
		int index = (page - 1) * PAGE_SIZE;

		// Get the sorted balances for the queried currency.
		List<Account> balances = AccountManager.sortAccountsByBalance(currency).stream().distinct().collect(Collectors.toList());

		// If the index is higher than the length of balances, tell the player that page doesn't exist.
		if (balances.size() <= index) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cPage " + page + " does not exist.",
					isPlayer)));
			return -1;
		}

		List<Account> pageBalances;

		// Get the data for the specified page.
		if (balances.size() - 1 < PAGE_SIZE + index) {
			pageBalances = balances.subList(index, balances.size());
		} else {
			pageBalances = balances.subList(index, PAGE_SIZE);
		}

		// Calculate the total pages based on the balances received.
		int pages = (int) Math.ceil((double)balances.size() / (double) PAGE_SIZE);

		// Create the output string.
		String output = "§7=== §eBalance Top §7(§b" + currency.getPlural() + "§7) ===\n\n";

		// Add the sorted balances to the string.
		for (int i = 0; i < pageBalances.size(); i++) {
			Account account = pageBalances.get(i);
			output += "§7" + (index + i + 1)  + " - §b" + account.getUsername() + "§7: §f" + account.getBalance(currency) +
					"§a\n";
		}
		// Add the page number to the string.
		output += "§7Page " + page + "/" + pages;

		// Send the output string to the sender.
		context.getSource().sendMessage(Text.literal(Utils.formatMessage(output,
				isPlayer)));
		return 1;
	}

}
