package com.bencrow11.multieconomy.command.commands;

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

public abstract class PayCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
	                            CommandRegistryAccess commandRegistryAccess,
	                            CommandManager.RegistrationEnvironment registrationEnvironment) {

		LiteralCommandNode<ServerCommandSource> root = CommandManager
				.literal("pay")
				.executes(PayCommand::showUsage)
				.then(CommandManager.argument("player", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							CommandSource.suggestMatching(ctx.getSource().getPlayerNames(), builder);
							return builder.buildFuture();
						})
						.executes(PayCommand::showUsage)
						.then(CommandManager.argument("amount", FloatArgumentType.floatArg())
								.suggests((ctx, builder) -> {
									CommandSource.suggestMatching(new String[]{"1", "10", "100", "1000"}, builder);
									return builder.buildFuture();
								})
						.executes(PayCommand::run)
										.then(CommandManager.argument("currency", StringArgumentType.string())
												.suggests((ctx, builder) -> {
													CommandSource.suggestMatching(ConfigManager.getConfig().getCurrenciesAsString()
															, builder);
													return builder.buildFuture();
												})
												.executes(PayCommand::run))))
				.build();

		dispatcher.getRoot().addChild(root);
	}

	public static int run(CommandContext<ServerCommandSource> context) {
		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.PAY_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission §b" +
						PermissionManager.PAY_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		String playerArg = StringArgumentType.getString(context, "player");
		float amountArg = FloatArgumentType.getFloat(context, "amount");
		boolean targetIsOnline = context.getSource().getPlayerNames().contains(playerArg);

		if (!ConfigManager.getConfig().isAllowOfflinePayments()) {
			if (!targetIsOnline) {
				context.getSource().sendMessage(Text.literal("§cYou can not pay offline players."));
				return -1;
			}
		}

		if (playerSource.getEntityName().equalsIgnoreCase(playerArg)) {
			context.getSource().sendMessage(Text.literal("§cYou can not pay yourself."));
			return -1;
		}

		if (!AccountManager.hasAccount(playerArg)) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cPlayer " + playerArg + " doesn't " +
					"exist.", isPlayer)));
			return -1;
		}

		if (amountArg <= 0) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cAmount must be greater than 0.", isPlayer)));
			return -1;
		}

		int argLength = context.getInput().split(" ").length;

		Currency currency = null;

		if (argLength == 3) {
			currency = ConfigManager.getConfig().getCurrencyByName(ConfigManager.getConfig().getDefaultCurrency());
		}

		if (argLength == 4) {
			currency = ConfigManager.getConfig().getCurrencyByName(StringArgumentType.getString(context, "currency"));
		}

		if (currency == null) {
			if (argLength == 3) {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cCurrency " +
						ConfigManager.getConfig().getDefaultCurrency() + " doesn't exist.",	isPlayer)));
				return -1;
			} else {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cCurrency " +
						StringArgumentType.getString(context, "currency") + " doesn't exist.",	isPlayer)));
				return -1;
			}
		}

		if (!currency.isAllowPayments()) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cYou can not pay players with" +
					" this currency.",
					isPlayer)));
			return -1;
		}

		if (AccountManager.getAccount(playerSource.getUuid()).getBalance(currency) < amountArg) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cYou do not have enough money in " +
							currency.getPlural() + " to pay.",
					isPlayer)));
			return -1;
		}

		boolean successRemove = AccountManager.getAccount(playerSource.getUuid()).remove(currency, amountArg);
		boolean successAdd = AccountManager.getAccount(playerArg).add(currency, amountArg);

		if (successAdd && successRemove) {
			if (amountArg == 1) {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§aSuccessfully paid §b" +
						amountArg + " " + currency.getSingular() + "§a to §b" + playerArg + "§a.", isPlayer)));
			} else {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§aSuccessfully paid §b" +
						amountArg + " " + currency.getPlural() + "§a to §b" + playerArg + "§a.", isPlayer)));

			}

			if (targetIsOnline) {
				context.getSource().getServer().getPlayerManager().getPlayer(playerArg).sendMessage(
						Text.literal("§b" + playerSource.getDisplayName().getString() + "§a paid you §b" + amountArg +
								" " + (amountArg == 1  ?  currency.getSingular() : currency.getPlural()) + "§a."));
			}
			return 1;
		}

		context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cUnable to pay player.",
				isPlayer)));
		return -1;
	}

	public static int showUsage(CommandContext<ServerCommandSource> context) {

		String usage = "§9§lMultiEconomy Command Usage - §r§3pay\n" +
				"§3> Pays another player\n" +
				"§9Arguments:\n" +
				"§3- §8<§7player§8> §3-> §7the player to pay\n" +
				"§3- §8<§7amount§8> §3-> §7the amount to pay\n" +
				"§3- §8[§7currency§8] §3-> §7the currency to pay in\n";

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(usage, context.getSource().isExecutedByPlayer())));
		return 1;
	}
}
