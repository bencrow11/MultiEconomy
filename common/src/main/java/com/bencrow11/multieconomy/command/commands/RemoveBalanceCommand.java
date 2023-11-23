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
import com.bencrow11.multieconomy.command.SubCommandInterface;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.permission.PermissionManager;
import com.bencrow11.multieconomy.util.Utils;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

/**
 * Creates the command "/meco remove" in game.
 */
public class RemoveBalanceCommand implements SubCommandInterface {

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("remove")
				.executes(this::showUsage)
				.then(Commands.argument("player", EntityArgument.players())
//						.suggests((ctx, builder) -> {
//							for (String name : ctx.getSource().getOnlinePlayerNames()) {
//								builder.suggest(name);
//							}
//							return builder.buildFuture();
//						})
						.executes(this::showUsage)
						.then(Commands.argument("currency", StringArgumentType.string())
								.suggests((ctx, builder) -> {
									for (String name : ConfigManager.getConfig().getCurrenciesAsString()) {
										builder.suggest(name);
									}
									return builder.buildFuture();
								})
								.executes(this::showUsage)
								.then(Commands.argument("amount", FloatArgumentType.floatArg())
										.suggests((ctx, builder) -> {
											for (int i = 1; i <= 1000; i = i * 10) {;
												builder.suggest(i);
											}
											return builder.buildFuture();
										})
										.executes(this::run))))
				.build();
	}

	/**
	 * Method to perform the logic when the command is executed.
	 * @param context the source of the command.
	 * @return integer to complete command.
	 */
	public int run(CommandContext<CommandSourceStack> context) {

		boolean isPlayer = context.getSource().isPlayer();
		ServerPlayer playerSource = context.getSource().getPlayer();

		// If the source is a player, check for a permission.
		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUUID(), PermissionManager.REMOVE_BALANCE_PERMISSION)) {
				context.getSource().sendSystemMessage(Component.literal("§cYou need the permission §b" +
						PermissionManager.REMOVE_BALANCE_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

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

	private int pay(CommandContext<CommandSourceStack> context, ServerPlayer playerSource, boolean isPlayer, String playerArg) {
		String currencyArg = StringArgumentType.getString(context, "currency");
		float amountArg = FloatArgumentType.getFloat(context, "amount");



		// Check the player has an account.
		if (!AccountManager.hasAccount(playerArg)) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cPlayer " + playerArg + " doesn" +
					"'t " +
					"exist.", isPlayer)));
			return -1;
		}

		// Get the currency
		Currency currency = ConfigManager.getConfig().getCurrencyByName(currencyArg);

		// Checks the currency exists.
		if (currency == null) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cCurrency " + currencyArg +
					" doesn't exist.", isPlayer)));
			return -1;
		}

		// Checks for valid amount.
		if (amountArg <= 0) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cAmount must be greater than 0.", isPlayer)));
			return -1;
		}

		// Checks the target player has enough money to remove the amount given in the argument.
		if (AccountManager.getAccount(playerArg).getBalance(currency) < amountArg) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cThis user doesn't have enough §b"  + currency.getPlural() +
					"§c to remove§b " +  amountArg + " §cfrom their account.", isPlayer)));
			return -1;
		}

		// Remove the balance from the player.
		boolean success = AccountManager.getAccount(playerArg).remove(currency, amountArg);

		// If successful, inform the sender.
		if (success) {
			if (amountArg == 1) {
				context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§aSuccessfully removed §b" +
						amountArg + " " + currency.getSingular() + "§a from §b" + playerArg + "§a's account.", isPlayer)));
			} else {
				context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§aSuccessfully removed §b" +
						amountArg + " " + currency.getPlural() + "§a from §b" + playerArg + "§a's account.", isPlayer)));
			}
			return 1;
		}

		// Tell the sender that removing the balance wasn't possible.
		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cUnable to remove currency from the " +
				"account.", isPlayer)));
		return -1;
	}

	/**
	 * Method used to show the usage of the command.
	 * @param context the source of the command
	 * @return integer to complete command.
	 */
	public int showUsage(CommandContext<CommandSourceStack> context) {

		String usage = "§9§lMultiEconomy Command Usage - §r§3remove\n" +
				"§3> Removes money from a currency on a players account\n" +
				"§9Arguments:\n" +
				"§3- §8<§7player§8> §3-> §7the player to remove the money from\n" +
				"§3- §8<§7currency§8> §3-> §7the currency to remove the amount from\n" +
				"§3- §8<§7amount§8> §3-> §7the amount to remove from the account\n";

		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(usage,
				context.getSource().isPlayer())));
		return 1;
	}
}
