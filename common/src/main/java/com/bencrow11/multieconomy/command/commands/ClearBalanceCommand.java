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
 * Creates the command "/meco clear" in game.
 */
public class ClearBalanceCommand implements SubCommandInterface {

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("clear")
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
								.executes(this::run)))
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
			if (!PermissionManager.hasPermission(playerSource.getUUID(), PermissionManager.CLEAR_BALANCE_PERMISSION)) {
				context.getSource().sendSystemMessage(Component.literal("§cYou need the permission §b" +
						PermissionManager.CLEAR_BALANCE_PERMISSION +
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

		// Check the player has an account.
		if (!AccountManager.hasAccount(playerArg)) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cPlayer " + playerArg + " doesn't " +
					"exist.", isPlayer)));
			return -1;
		}

		// Gets the currency.
		Currency currency = ConfigManager.getConfig().getCurrencyByName(currencyArg);

		// Checks the currency exists.
		if (currency == null) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cCurrency " + currencyArg +
					" doesn't exist.", isPlayer)));
			return -1;
		}

		// Sets the players balance of the currency to 0.
		boolean success = AccountManager.getAccount(playerArg).set(currency, 0);

		// If successful, inform sender.
		if (success) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§aSuccessfully cleared §b" +
					playerArg + "§a's balance for §b" + currency.getName() + "§a.", isPlayer)));
			return 1;
		}

		// Inform user that something went wrong.
		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cUnable to clear the accounts balance.",
				isPlayer)));
		return -1;
	}

	/**
	 * Method used to show the usage of the command.
	 * @param context the source of the command
	 * @return integer to complete command.
	 */
	public int showUsage(CommandContext<CommandSourceStack> context) {

		String usage = "§9§lMultiEconomy Command Usage - §r§3clear\n" +
				"§3> Clears money of a currency on a players account\n" +
				"§9Arguments:\n" +
				"§3- §8<§7player§8> §3-> §7the player to set the money to\n" +
				"§3- §8<§7currency§8> §3-> §7the currency to set the amount to\n";

		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(usage, context.getSource().isPlayer())));
		return 1;
	}
}
