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
				usage = usage +
						"> /meco add <player> <currency> <amount>\n" +
						"> /meco remove <player> <currency> <amount>\n" +
						"> /meco set <player> <currency> <amount>\n" +
						"> /meco clear <player> <currency>\n";
			}
		} else {
			usage = usage +
					"> /meco add <player> <currency> <amount>\n" +
					"> /meco remove <player> <currency> <amount>\n" +
					"> /meco set <player> <currency> <amount>\n" +
					"> /meco clear <player> <currency>\n";
		}
		usage = usage +
				"> /meco help";

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(usage, isPlayer)));
		return 1;
	}
}
