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
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ClearBalanceCommand implements SubCommandInterface {

	@Override
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("clear")
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
								}).executes(this::run)))
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.CLEAR_BALANCE_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission §b" +
						PermissionManager.CLEAR_BALANCE_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		String playerArg = StringArgumentType.getString(context, "player");
		String currencyArg = StringArgumentType.getString(context, "currency");

		// Check the player has an account.
		if (!AccountManager.hasAccount(playerArg)) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cPlayer " + playerArg + " doesn't " +
					"exist.", isPlayer)));
			return -1;
		}

		Currency currency = ConfigManager.getConfig().getCurrencyByName(currencyArg);

		// Checks the currency exists.
		if (currency == null) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cCurrency " + currencyArg +
					" doesn't exist.", isPlayer)));
			return -1;
		}

		boolean success = AccountManager.getAccount(playerArg).set(currency, 0);

		if (success) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§aSuccessfully cleared §b" +
					playerArg + "§a's balance for §b" + currency.getName() + "§a.", isPlayer)));
			return 1;
		}

		context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cUnable to clear the accounts balance.",
				isPlayer)));
		return -1;
	}

	public int showUsage(CommandContext<ServerCommandSource> context) {

		String usage = "§9§lMultiEconomy Command Usage - §r§3clear\n" +
				"§3> Clears money of a currency on a players account\n" +
				"§9Arguments:\n" +
				"§3- §8<§7player§8> §3-> §7the player to set the money to\n" +
				"§3- §8<§7currency§8> §3-> §7the currency to set the amount to\n";

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(usage, context.getSource().isExecutedByPlayer())));
		return 1;
	}
}
