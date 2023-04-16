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
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Creates the command "/bal [player]" in game.
 */
public abstract class BalCommand {
	/**
	 * Method to register and build the command.
	 */
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
	                            CommandRegistryAccess commandRegistryAccess,
	                            CommandManager.RegistrationEnvironment registrationEnvironment) {

		LiteralCommandNode<ServerCommandSource> root = CommandManager
				.literal("balance")
				.executes(BalCommand::run)
				.then(CommandManager.argument("player", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							CommandSource.suggestMatching(ctx.getSource().getPlayerNames(), builder);
							return builder.buildFuture();
						})
						.executes(BalCommand::run))
				.build();

		dispatcher.getRoot().addChild(root);

		// Adds alias bal to balance
		dispatcher.register(CommandManager.literal("bal").redirect(root).executes(BalCommand::run));
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
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.BAL_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission §b" +
						PermissionManager.BAL_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		// Counts the amount of arguments given.
		int argLength = context.getInput().split(" ").length;

		Account account = null;

		// If only one argument is given (bal) then display the senders balances.
		if (argLength == 1) {
			account = AccountManager.getAccount(context.getSource().getPlayer().getUuid());
		}

		// If two arguments are given, get the balance of the player given and display it.
		if (argLength == 2) {
			String playerArg = StringArgumentType.getString(context, "player");

			// If the player doesn't have an account, tell the sender the player doesn't exist.
			if (!AccountManager.hasAccount(playerArg)) {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cPlayer " + playerArg + " doesn't " +
						"exist.", isPlayer)));
				return -1;
			}

			account = AccountManager.getAccount(playerArg);
		}

		// Create the string to send back to the sender.
		String output = "§7=== §eBalances §7(§b" + account.getUsername() + "§7) ===\n\n";

		// Add each balance on the account to the string.
		for (Currency balance : account.getBalances().keySet()) {
			output += "§7- §b" + balance.getPlural() + "§7: §f" + account.getBalance(balance) + "\n";
		}

		// Send the balances to the sender.
		context.getSource().sendMessage(Text.literal(Utils.formatMessage(output,
				isPlayer)));
		return 1;
	}

}
