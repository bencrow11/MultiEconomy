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

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.MultiEconomy;
import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.command.SubCommandInterface;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.permission.PermissionManager;
import com.bencrow11.multieconomy.util.Utils;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Creates the command "/meco reload" in game.
 */
public class ReloadCommand implements SubCommandInterface {

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("reload")
				.executes(this::run)
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
			if (!PermissionManager.hasPermission(playerSource.getUUID(), PermissionManager.RELOAD_PERMISSION)) {
				context.getSource().sendSystemMessage(Component.literal("§cYou need the permission §b" +
						PermissionManager.RELOAD_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		// Tell the sender the mod is being reloaded.
		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§aReloading MultiEconomy.",
				isPlayer)));

		// Reload the mod.
		ConfigManager.loadConfig(); // Loads the config from file.
		AccountManager.initialise(); // Adds saved accounts to memory.
		MultiEconomy.LOGGER.info("MultiEconomy reloaded.");
		ErrorManager.printErrorsToConsole();

		// If there are any errors, inform the player.
		if (ErrorManager.getErrors().size() != 0) {
			context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§cErrors occurred while reloading.",
					isPlayer)));
			if (isPlayer) {
				ErrorManager.printErrorsToPlayer(context.getSource().getPlayer());
			} else {
				ErrorManager.printErrorsToConsole();
			}
			return -1;
		}

		// Tell the player that the mod has been reloaded.
		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("§aMultiEconomy reloaded successfully.",
				isPlayer)));
		return 1;
	}
}
