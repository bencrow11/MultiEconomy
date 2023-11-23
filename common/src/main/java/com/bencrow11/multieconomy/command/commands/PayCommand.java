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

import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.permission.PermissionManager;
import com.bencrow11.multieconomy.util.Utils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Creates the command "/pay <player> <amount> [currency]" in game.
 */
public abstract class PayCommand {
	/**
	 * Method to register and build the command.
	 */
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		createCommand(dispatcher);
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
		createCommand(dispatcher);
	}

	private static void createCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralCommandNode<CommandSourceStack> root = Commands
				.literal("pay")
				.executes(PayCommand::showUsage)
				.then(Commands.argument("player", EntityArgument.players())
//						.suggests((ctx, builder) -> {
//							for (String name : ctx.getSource().getOnlinePlayerNames()) {
//								builder.suggest(name);
//							}
//							return builder.buildFuture();
//						})
						.executes(PayCommand::showUsage)
						.then(Commands.argument("amount", FloatArgumentType.floatArg())
								.suggests((ctx, builder) -> {
									for (int i = 1; i <= 1000; i = i * 10) {;
										builder.suggest(i);
									}
									return builder.buildFuture();
								})
								.executes(PayCommand::run)
								.then(Commands.argument("currency", StringArgumentType.string())
										.suggests((ctx, builder) -> {
											for (String name : ConfigManager.getConfig().getCurrenciesAsString()) {
												builder.suggest(name);
											}
											return builder.buildFuture();
										})
										.executes(PayCommand::run))))
				.build();

		dispatcher.getRoot().addChild(root);
	}

	/**
	 * Method that's used to execute the functionality for the command.
	 * @param context the source of the command.
	 * @return integer to complete the command.
	 */
	public static int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		boolean isPlayer = context.getSource().isPlayer();
		ServerPlayer playerSource = context.getSource().getPlayer();

		// If the source is a player, check for permission.
		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUUID(), PermissionManager.PAY_PERMISSION)) {
				context.getSource().sendSystemMessage(Component.literal("§cYou need the permission §b" +
						PermissionManager.PAY_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		// Collects the arguments from the command.
		List<ServerPlayer> playerArguments;
		try {
			playerArguments = EntityArgument.getPlayers(context, "player").stream().toList();
			playerArguments.forEach(player -> pay(context, playerSource, isPlayer, player.getDisplayName().getString()));
		} catch (CommandSyntaxException ex) {
			String playerArg = context.getInput().split(" ")[1];
			pay(context, playerSource, isPlayer, playerArg);
		}
		return -1;
	}

	private static int pay(CommandContext<CommandSourceStack> context, ServerPlayer playerSource, boolean isPlayer, String playerArg) {
		float amountArg = FloatArgumentType.getFloat(context, "amount");

		// Checks the player is online
		boolean targetIsOnline = context.getSource().getOnlinePlayerNames().contains(playerArg);

		// If offline payments are disabled, and the target player is offline, tell the sender.
		if (!ConfigManager.getConfig().isAllowOfflinePayments()) {
			if (!targetIsOnline) {
				context.getSource().sendSystemMessage(Component.literal("§cYou can not pay offline players."));
				return -1;
			}
		}

		// If the target player is also the sender (paying themselves), tell them this isn't allowed.
		if (playerSource.getName().toString().equalsIgnoreCase(playerArg)) {
			context.getSource().sendSystemMessage(Component.literal("§cYou can not pay yourself."));
			return -1;
		}

		// If the target doesn't have an account, tell the sender.
		if (!AccountManager.hasAccount(playerArg)) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cPlayer " + playerArg + " doesn't " +
					"exist.", isPlayer)));
			return -1;
		}

		// If the amount to pay is less than or equal to 0, tell the player they can not do this.
		if (amountArg <= 0) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cAmount must be greater than 0.", isPlayer)));
			return -1;
		}

		// Count the amount of arguments.
		int argLength = context.getInput().split(" ").length;

		Currency currency = null;

		// If there are three arguments, use the default currency.
		if (argLength == 3) {
			currency = ConfigManager.getConfig().getCurrencyByName(ConfigManager.getConfig().getDefaultCurrency());
		}

		// If there are four arguments, get the currency of the one from the command.
		if (argLength == 4) {
			currency = ConfigManager.getConfig().getCurrencyByName(StringArgumentType.getString(context, "currency"));
		}

		// If the currency doesn't exist, tell the sender the currency doesn't exist.
		if (currency == null) {
			if (argLength == 3) {
				context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cCurrency " +
						ConfigManager.getConfig().getDefaultCurrency() + " doesn't exist.",	isPlayer)));
				return -1;
			} else {
				context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cCurrency " +
						StringArgumentType.getString(context, "currency") + " doesn't exist.",	isPlayer)));
				return -1;
			}
		}

		// If the currency does not allow payments, inform the sender.
		if (!currency.isAllowPayments()) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cYou can not pay players with" +
							" this currency.",
					isPlayer)));
			return -1;
		}

		// If the sender doesn't have enough money, tell them.
		if (AccountManager.getAccount(playerSource.getUUID()).getBalance(currency) < amountArg) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cYou do not have enough money in " +
							currency.getPlural() + " to pay.",
					isPlayer)));
			return -1;
		}

		// Remove money from the sender and add it to the target.
		boolean successRemove = AccountManager.getAccount(playerSource.getUUID()).remove(currency, amountArg);
		boolean successAdd = AccountManager.getAccount(playerArg).add(currency, amountArg);

		// If both transactions were successful, inform the sender and target.
		if (successAdd && successRemove) {
			if (amountArg == 1) {
				context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§aSuccessfully paid §b" +
						amountArg + " " + currency.getSingular() + "§a to §b" + playerArg + "§a.", isPlayer)));
			} else {
				context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§aSuccessfully paid §b" +
						amountArg + " " + currency.getPlural() + "§a to §b" + playerArg + "§a.", isPlayer)));

			}

			if (targetIsOnline) {
				context.getSource().getServer().getPlayerList().getPlayerByName(playerArg).sendSystemMessage(
						Component.literal("§b" + playerSource.getDisplayName().getString() + "§a paid you §b" + amountArg +
								" " + (amountArg == 1  ?  currency.getSingular() : currency.getPlural()) + "§a."));
			}
			return 1;
		}

		// Any other case, tell the player something went wrong.
		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cUnable to pay player.",
				isPlayer)));

		return -1;
	}

	/**
	 * Method used to show the usage of the command.
	 * @param context the source of the command
	 * @return integer to complete command.
	 */
	public static int showUsage(CommandContext<CommandSourceStack> context) {

		String usage = "§9§lMultiEconomy Command Usage - §r§3pay\n" +
				"§3> Pays another player\n" +
				"§9Arguments:\n" +
				"§3- §8<§7player§8> §3-> §7the player to pay\n" +
				"§3- §8<§7amount§8> §3-> §7the amount to pay\n" +
				"§3- §8[§7currency§8] §3-> §7the currency to pay in\n";

		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(usage,
				context.getSource().isPlayer())));
		return 1;
	}
}
