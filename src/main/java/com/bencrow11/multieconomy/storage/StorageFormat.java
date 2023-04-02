package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import com.bencrow11.multieconomy.currency.Currency;

import java.util.HashMap;

public class StorageFormat {
	private String owner;
	private HashMap<String, Float> balances = new HashMap<>();

	public StorageFormat(Account account) {
		owner = account.getOwner().toString();

		for (Currency currency : account.getBalances().keySet()) {
			balances.put(currency.getName(), account.getBalance(currency));
		}
	}

	public String getOwner() {
		return owner;
	}

	public HashMap<String, Float> getBalances() {
		return balances;
	}
}
