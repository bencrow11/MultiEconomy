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
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Creates the command "/meco add" in game.
 */
public class SetBalanceCommand implements SubCommandInterface {

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("set")
				.executes(this::showUsage)
				.then(CommandManager.argument("player", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							CommandSource.suggestMatching(ctx.getSource().getPlayerNames(), builder);
							return builder.buildFuture();
						})
						.executes(this::showUsage)
						.then(CommandManager.argument("currency", StringArgumentType.string())
								.suggests((ctx, builder) -> {
									CommandSource.suggestMatching(ConfigManager.getConfig().getCurrenciesAsString()
											, builder);
									return builder.buildFuture();
								})
								.executes(this::showUsage)
								.then(CommandManager.argument("amount", FloatArgumentType.floatArg())
										.suggests((ctx, builder) -> {
											CommandSource.suggestMatching(new String[]{"1", "10", "100", "1000"}, builder);
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
	public int run(CommandContext<ServerCommandSource> context) {

		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		// If the source is a player, check for a permission.
		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.SET_BALANCE_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission §b" +
						PermissionManager.SET_BALANCE_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		// Collect the arguments from the command.
		String playerArg = StringArgumentType.getString(context, "player");
		String currencyArg = StringArgumentType.getString(context, "currency");
		float amountArg = FloatArgumentType.getFloat(context, "amount");

		// Check the player has an account.
		if (!AccountManager.hasAccount(playerArg)) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cPlayer " + playerArg + " doesn't " +
					"exist.", isPlayer)));
			return -1;
		}

		// Get the currency.
		Currency currency = ConfigManager.getConfig().getCurrencyByName(currencyArg);

		// Checks the currency exists.
		if (currency == null) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cCurrency " + currencyArg +
					" doesn't exist.", isPlayer)));
			return -1;
		}

		// Checks for valid amount.
		if (amountArg <= 0) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cAmount must be greater than 0.", isPlayer)));
			return -1;
		}

		// Set the players balance.
		boolean success = AccountManager.getAccount(playerArg).set(currency, amountArg);

		// If successful, inform the sender.
		if (success) {
			if (amountArg == 1) {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§aSuccessfully set §b" +
						playerArg + "§a's account to §b" + amountArg + " " + currency.getSingular() + "§a.", isPlayer)));
			} else {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§aSuccessfully set §b" +
						playerArg + "§a's account to §b" + amountArg + " " + currency.getPlural() + "§a.", isPlayer)));
			}
			return 1;
		}

		// Inform sender that something went wrong.
		context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cUnable to set the accounts balance.", isPlayer)));
		return -1;
	}

	/**
	 * Method used to show the usage of the command.
	 * @param context the source of the command
	 * @return integer to complete command.
	 */
	public int showUsage(CommandContext<ServerCommandSource> context) {

		String usage = "§9§lMultiEconomy Command Usage - §r§3set\n" +
				"§3> Sets the amount of a currency on a players account\n" +
				"§9Arguments:\n" +
				"§3- §8<§7player§8> §3-> §7the player to set the money on\n" +
				"§3- §8<§7currency§8> §3-> §7the currency to set the amount to\n" +
				"§3- §8<§7amount§8> §3-> §7the amount to set on the account\n";

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(usage, context.getSource().isExecutedByPlayer())));
		return 1;
	}
}