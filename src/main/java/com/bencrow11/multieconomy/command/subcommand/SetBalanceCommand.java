package com.bencrow11.multieconomy.command.subcommand;

import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.command.SubCommandInterface;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.permission.PermissionManager;
import com.bencrow11.multieconomy.util.Utils;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SetBalanceCommand implements SubCommandInterface {

	@Override
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("set")
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
								})
								.executes(this::showUsage)
								.then(CommandManager.argument("amount", FloatArgumentType.floatArg())
										.executes(this::run))))
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		boolean isPlayer = context.getSource().isExecutedByPlayer();
		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		if (isPlayer) {
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.SET_BALANCE_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission §b" +
						PermissionManager.SET_BALANCE_PERMISSION +
						"§c to run this command."));
				return -1;
			}
		}

		String playerArg = StringArgumentType.getString(context, "player");
		String currencyArg = StringArgumentType.getString(context, "currency");
		float amountArg = FloatArgumentType.getFloat(context, "amount");



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

		// Checks for valid amount.
		if (amountArg <= 0) {
			context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cAmount must be greater than 0.", isPlayer)));
			return -1;
		}

		boolean success = AccountManager.getAccount(playerArg).set(currency, amountArg);

		if (success) {
			if (amountArg == 1) {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§aSuccessfully set §b" +
						playerArg + "§a's account to §b" + amountArg + " " + currency.getSingular() + "§a.", isPlayer)));
			} else {
				context.getSource().sendMessage(Text.literal(Utils.formatMessage("§aSuccessfully set §b" +
						playerArg + "§a's account to §b" + amountArg + " " + currency.getPlural() + "§a.", isPlayer)));
			}
			return 1;
		}

		context.getSource().sendMessage(Text.literal(Utils.formatMessage("§cUnable to set the accounts balance.", isPlayer)));
		return -1;
	}

	public int showUsage(CommandContext<ServerCommandSource> context) {

		String usage = "§9§lMultiEconomy Command Usage - §r§3set\n" +
				"§3> Sets the amount of a currency on a players account\n" +
				"§9Arguments:\n" +
				"§3- §8<§7player§8> §3-> §7the player to set the money on\n" +
				"§3- §8<§7currency§8> §3-> §7the currency to set the amount to\n" +
				"§3- §8<§7amount§8> §3-> §7the amount to set on the account\n";

		context.getSource().sendMessage(Text.literal(Utils.formatMessage(usage, context.getSource().isExecutedByPlayer())));
		return 1;
	}
}
