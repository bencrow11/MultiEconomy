package com.bencrow11.multieconomy.account;

import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public class Account {
	private final PlayerEntity owner;
	private HashMap<Currency, Float> balances;

	public Account(PlayerEntity owner) {
		this.owner = owner;

		for (Currency currency : ConfigManager.getConfig().getCurrencies()) {
			balances.put(currency, currency.getStartBalance());
		}
	}

	public PlayerEntity getOwner() {
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
		return true;
	}

	public boolean remove(Currency currency, float amount) {
		if (balances.get(currency) < amount) {
			return false;
		}
		float oldAmount = balances.get(currency);
		balances.remove(currency);
		balances.put(currency, oldAmount - amount);
		return true;
	}

	public boolean set(Currency currency, float amount) {
		balances.remove(currency);
		balances.put(currency, amount);
		return true;
	}
}