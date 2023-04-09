package com.bencrow11.multieconomy.command.subcommand;

import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.permission.PermissionManager;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AddBalanceCommand implements SubCommand {

	@Override
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("add")
				.then(CommandManager.argument("player", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							CommandSource.suggestMatching(ctx.getSource().getPlayerNames(), builder);
							return builder.buildFuture();
						})
						.then(CommandManager.argument("currency", StringArgumentType.string())
								.suggests((ctx, builder) -> {
									CommandSource.suggestMatching(ConfigManager.getConfig().getCurrenciesAsString()
											, builder);
									return builder.buildFuture();
								})
								.then(CommandManager.argument("amount", FloatArgumentType.floatArg())
										.executes(this::run))))
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

		ServerPlayerEntity playerSource = context.getSource().getPlayer();

		if (playerSource != null) {
			if (!PermissionManager.hasPermission(playerSource.getUuid(), PermissionManager.ADD_BALANCE_PERMISSION)) {
				context.getSource().sendMessage(Text.literal("§cYou need the permission " +
						PermissionManager.ADD_BALANCE_PERMISSION +
						" to run this command."));
				return -1;
			}
		}

		String playerArg = StringArgumentType.getString(context, "player");
		String currencyArg = StringArgumentType.getString(context, "currency");
		float amountArg = FloatArgumentType.getFloat(context, "amount");



		// Check the player has an account.
		if (!AccountManager.hasAccount(playerArg)) {
			context.getSource().sendMessage(Text.literal("§cPlayer " + playerArg + " doesn't exist."));
			return -1;
		}

		Currency currency = ConfigManager.getConfig().getCurrencyByName(currencyArg);

		// Checks the currency exists.
		if (currency == null) {
			context.getSource().sendMessage(Text.literal("§cCurrency " + currencyArg +
					" doesn't exist."));
			return -1;
		}

		// Checks for valid amount.
		if (amountArg <= 0) {
			context.getSource().sendMessage(Text.literal("§cAmount must be greater than 0."));
			return -1;
		}

		boolean success = AccountManager.getAccount(playerArg).add(currency, amountArg);

		System.out.println("Adding currency: " + success);

		if (success) {
			if (amountArg == 1) {
				context.getSource().sendMessage(Text.literal("§2Successfully added §b" +
						amountArg + " " + currency.getSingular() + "§2 to §b" + playerArg + "§2's account."));
			} else {
				context.getSource().sendMessage(Text.literal("§2Successfully added §b" +
						amountArg + " " + currency.getPlural() + "§2 to §b" + playerArg + "§2's account."));
			}
			return 1;
		}

		context.getSource().sendMessage(Text.literal("§cUnable to add currency to the account."));
		return -1;
	}
}
