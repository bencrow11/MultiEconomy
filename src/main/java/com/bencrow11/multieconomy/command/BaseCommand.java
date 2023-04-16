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

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.command.commands.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public abstract class BaseCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
	                            CommandRegistryAccess commandRegistryAccess,
	                            CommandManager.RegistrationEnvironment registrationEnvironment) {

		LiteralCommandNode<ServerCommandSource> root = CommandManager
				.literal(Multieconomy.BASE_COMMAND)
				.executes(BaseCommand::run)
				.build();

		dispatcher.getRoot().addChild(root);


		for (String alias : Multieconomy.ALIASES) {
			dispatcher.register(CommandManager.literal(alias).redirect(root).executes(BaseCommand::run));
		}

		root.addChild(new AddBalanceCommand().build());
		root.addChild(new RemoveBalanceCommand().build());
		root.addChild(new SetBalanceCommand().build());
		root.addChild(new ClearBalanceCommand().build());
		root.addChild(new HelpCommand().build());
		root.addChild(new ReloadCommand().build());
	}

	public static int run(CommandContext<ServerCommandSource> context) {
		context.getSource().sendMessage(Text.literal("§aRunning §bMultiEconomy §b" + Multieconomy.VERSION +
				"§a."));
		return 1;
	}
}
