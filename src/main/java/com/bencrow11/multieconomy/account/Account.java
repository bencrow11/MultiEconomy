package com.bencrow11.multieconomy.account;

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.storage.StorageFormat;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Account {
	private final UUID owner;
	private HashMap<Currency, Float> balances = new HashMap<>();

	public Account(UUID owner) {
		this.owner = owner;

		for (Currency currency : ConfigManager.getConfig().getCurrencies()) {
			balances.put(currency, currency.getStartBalance());
		}
	}

	public Account(StorageFormat account) {
		this.owner = UUID.fromString(account.getOwner());

		HashMap<String, Float> storedBalances = account.getBalances();

		ArrayList<Currency> configCurrencies = ConfigManager.getConfig().getCurrencies();

		for (String currencyName : storedBalances.keySet()) {
			boolean found = false;

			for (Currency configCurrency : configCurrencies) {
				if (configCurrency.getName().equals(currencyName)) {
					balances.put(configCurrency, storedBalances.get(currencyName));
					found = true;
					break;
				}
			}

			if (!found) {
				Multieconomy.LOGGER.error("Currency " + currencyName + " was not found in the config.");
			}
		}

	}

	public UUID getOwner() {
		return owner;
	}

	public HashMap<Currency, Float> getBalances() {
		return balances;
	}

	public float getBalance(Currency currency) {
		return balances.get(currency);
	}

	public boolean add(Currency currency, float amount) {
		float oldAmount = balances.get(currency);
		balances.remove(currency);
		balances.put(currency, oldAmount + amount);
		AccountManager.updateAccount(this);
		return true;
	}

	public boolean remove(Currency currency, float amount) {
		if (balances.get(currency) < amount) {
			return false;
		}
		float oldAmount = balances.get(currency);
		balances.remove(currency);
		balances.put(currency, oldAmount - amount);
		AccountManager.updateAccount(this);
		return true;
	}

	public boolean set(Currency currency, float amount) {
		balances.remove(currency);
		balances.put(currency, amount);
		AccountManager.updateAccount(this);
		return true;
	}

	@Override
	public String toString() {
		String base = this.getOwner().toString() + ": \n";

		for (Currency currency : getBalances().keySet()) {
			base = base + "Currency:\n" + currency.getName() + getBalance(currency) + "\n";
		}

		return base;
	}
}
