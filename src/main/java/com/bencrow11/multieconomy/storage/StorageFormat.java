package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import com.bencrow11.multieconomy.currency.Currency;

import java.util.HashMap;

/**
 *  Class that represents the data stored for a single player.
 *  Converts an account to store as a JSON.
 */
public class StorageFormat {
	private final String owner; //String of the owners UUID.
	private HashMap<String, Float> balances = new HashMap<>(); //Map that holds the owners balances.

	/**
	 * Constructor to convert an account to a StorageFormat object.
	 * @param account the account that needs to be saved to file.
	 */
	public StorageFormat(Account account) {
		owner = account.getOwner().toString();

		// Converts currency names to string with their balance.
		for (Currency currency : account.getBalances().keySet()) {
			balances.put(currency.getName(), account.getBalance(currency));
		}

		// Adds the unavailable balances to the balances list to save.
		for (String unavailableCurrency : account.getUnavailableBalances().keySet()) {
			balances.put(unavailableCurrency, account.getUnavailableBalances().get(unavailableCurrency));
		}
	}

	/**
	 * Getter for the owner field.
	 * @return string that represents the owner of the account.
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Getter for the balances field.
	 * @return HashMap that represents the owners balances for each currency.
	 */
	public HashMap<String, Float> getBalances() {
		return balances;
	}
}
