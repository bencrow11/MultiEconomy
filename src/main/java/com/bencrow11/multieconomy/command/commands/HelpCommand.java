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

public class HelpCommand implements SubCommandInterface {

	@Override
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("help").executes(this::run).build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();
		String usage = "§2MultiEconomy Commands:\n";

		if (isPlayer) {
			if (PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.HELP_EXTRAS_PERMISSION)) {
				usage = usage + adminHelp();

			}
		} else {
			usage = usage + adminHelp();
		}
		usage = usage +
				"> /meco help\n" +
				"> /pay <player> <amount> [currency]\n" +
				"> /bal [player]\n" +
				"> /baltop [currency]";

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(usage, isPlayer)));
		return 1;
	}

	private String adminHelp() {
		return "> /meco add <player> <currency> <amount>\n" +
				"> /meco remove <player> <currency> <amount>\n" +
				"> /meco set <player> <currency> <amount>\n" +
				"> /meco clear <player> <currency>\n" +
				"> /meco reload\n";
	}
}
