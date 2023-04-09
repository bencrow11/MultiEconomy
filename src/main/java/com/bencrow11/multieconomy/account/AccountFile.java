package com.bencrow11.multieconomy.account;

import com.bencrow11.multieconomy.currency.Currency;

import java.util.HashMap;

public class AccountFile {
	private final String uuid;
	private final String username;
	private HashMap<String, Float> balances = new HashMap<>();


	public AccountFile(Account account) {
		this.uuid = account.getUUID().toString();
		this.username = account.getUsername().toLowerCase();

		// Adds the used currencies to a list, with their name as the identifier.
		for (Currency currency : account.getBalances().keySet()) {
			this.balances.put(currency.getName(), account.getBalance(currency));
		}

		// Adds the unused currencies to a list, with their name as the identifier.
		for (String unusedCurrency : account.getUnavailableBalances().keySet()) {
			this.balances.put(unusedCurrency, account.getUnusedBalance(unusedCurrency));
		}

	}

	/**
	 * Getter for UUID.
	 * @return string that represents the UUID.
	 */
	public String getUUID() {
		return uuid;
	}

	/**
	 * Getter for username.
	 * @return string that represents the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Getter for the balances.
	 * @return HashMap with balances.
	 */
	public HashMap<String, Float> getBalances() {
		return balances;
	}
}
