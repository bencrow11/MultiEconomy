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
package com.bencrow11.multieconomy.command;

import com.bencrow11.multieconomy.MultiEconomy;
import com.bencrow11.multieconomy.command.commands.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/**
 * Creates the command "/meco" in game.
 */
public abstract class BaseCommand {

	/**
	 * Method to register and build the command.
	 */
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
	                            CommandBuildContext commandBuildContext,
	                            Commands.CommandSelection commandSelection) {


		LiteralCommandNode<CommandSourceStack> root = Commands
				.literal(MultiEconomy.BASE_COMMAND)
				.executes(BaseCommand::run)
				.build();

		dispatcher.getRoot().addChild(root);

		// Add the aliases to the base command.
		for (String alias : MultiEconomy.ALIASES) {
			dispatcher.register(Commands.literal(alias).redirect(root).executes(BaseCommand::run));
		}

		// Add the subcommands.
		root.addChild(new AddBalanceCommand().build());
		root.addChild(new RemoveBalanceCommand().build());
		root.addChild(new SetBalanceCommand().build());
		root.addChild(new ClearBalanceCommand().build());
		root.addChild(new HelpCommand().build());
		root.addChild(new ReloadCommand().build());
	}

	// Runs when the base command is run with no subcommands.
	public static int run(CommandContext<CommandSourceStack> context) {
		context.getSource().sendSystemMessage(Component.literal("§aRunning §bMultiEconomy §b" + MultiEconomy.VERSION +
				"§a."));
		return 1;
	}
}
