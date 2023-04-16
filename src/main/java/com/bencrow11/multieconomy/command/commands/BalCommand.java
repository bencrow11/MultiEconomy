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

public abstract class BalCommand {
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

		dispatcher.register(CommandManager.literal("bal").redirect(root).executes(BalCommand::run));
	}

	public static int run(CommandContext<ServerCommandSource> context) {
		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.BAL_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission §b" +
						PermissionManager.BAL_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		int argLength = context.getInput().split(" ").length;

		Account account = null;

		if (argLength == 1) {
			account = AccountManager.getAccount(context.getSource().getPlayer().getUuid());
		}

		if (argLength == 2) {
			String playerArg = StringArgumentType.getString(context, "player");

			if (!AccountManager.hasAccount(playerArg)) {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cPlayer " + playerArg + " doesn't " +
						"exist.", isPlayer)));
				return -1;
			}

			account = AccountManager.getAccount(playerArg);
		}

		String output = "§7=== §eBalances §7(§b" + account.getUsername() + "§7) ===\n\n";

		for (Currency balance : account.getBalances().keySet()) {
			output += "§7- §b" + balance.getPlural() + "§7: §f" + account.getBalance(balance) + "\n";
		}

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(output,
				isPlayer)));
		return 1;
	}

}
