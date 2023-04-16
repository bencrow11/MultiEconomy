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

import com.bencrow11.multieconomy.command.SubCommandInterface;
import com.bencrow11.multieconomy.permission.PermissionManager;
import com.bencrow11.multieconomy.util.Utils;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Creates the command "/meco add" in game.
 */
public class HelpCommand implements SubCommandInterface {

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("help").executes(this::run).build();
	}

	/**
	 * Method to perform the logic when the command is executed.
	 * @param context the source of the command.
	 * @return integer to complete command.
	 */
	public int run(CommandContext<ServerCommandSource> context) {

		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		// Create the output string.
		String usage = "§2MultiEconomy Commands:\n";

		// If the source is a player, check for a permission to view admin commands.
		if (isPlayer) {
			if (PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.HELP_EXTRAS_PERMISSION)) {
				usage = usage + adminHelp(); // Add the admin commands to the output string.

			}
		} else {
			// If the sender isn't a payer, add the admin commands to the output string.
			usage = usage + adminHelp();
		}
		// Add standard commands to the output string.
		usage = usage +
				"> /meco help\n" +
				"> /pay <player> <amount> [currency]\n" +
				"> /bal [player]\n" +
				"> /baltop [currency]";

		// Send the output string to the player.
		context.getSource().sendMessage(Text.literal(Utils.formatMessage(usage, isPlayer)));
		return 1;
	}

	/**
	 * Method to store the admin help string in.
	 * @return String that holds the admin commands.
	 */
	private String adminHelp() {
		return "> /meco add <player> <currency> <amount>\n" +
				"> /meco remove <player> <currency> <amount>\n" +
				"> /meco set <player> <currency> <amount>\n" +
				"> /meco clear <player> <currency>\n" +
				"> /meco reload\n";
	}
}
