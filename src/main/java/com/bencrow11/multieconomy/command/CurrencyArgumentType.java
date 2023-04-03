package com.bencrow11.multieconomy.command;

import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class CurrencyArgumentType implements ArgumentType<Currency> {

	public static CurrencyArgumentType currency() {
		return new CurrencyArgumentType();
	}

	@Override
	public Currency parse(StringReader reader) throws CommandSyntaxException {
		int argBeginning = reader.getCursor();
		if (!reader.canRead()) {
			reader.skip();
		}

		while (reader.canRead() && reader.peek() != ' ') {
			reader.skip();
		}

		String currencyString = reader.getString().substring(argBeginning, reader.getCursor()).toLowerCase().trim();

		try {
			for (Currency currency : ConfigManager.getConfig().getCurrencies()) {
				if (currency.getName().toLowerCase().trim().equals(currencyString)) {
					return currency;
				}
			}
			throw new IllegalArgumentException("Currency doesn't exist");
		} catch (Exception e) {
			throw new SimpleCommandExceptionType(Text.literal(e.getMessage())).createWithContext(reader);
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return ArgumentType.super.listSuggestions(context, builder);
	}

	@Override
	public Collection<String> getExamples() {
		ArrayList<String> examples = new ArrayList<>();

		for (Currency currency : ConfigManager.getConfig().getCurrencies()) {
			examples.add(currency.getName());
		}

		return examples;
	}
}
