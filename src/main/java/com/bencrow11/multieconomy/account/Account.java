package com.bencrow11.multieconomy.account;

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.storage.StorageFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Account class that represents a single users account.
 * */
public class Account {
	private final UUID owner; // The UUID of the player who owns the account
	private HashMap<Currency, Float> balances = new HashMap<>(); // The balances for each currency
	private HashMap<String, Float> unavailableBalances = new HashMap<>(); // The balances that no longer exist

	/**
	 * Constructor for creating a new account. Used for new players.
	 * @param owner the UUID of the owner of the account.
	 */
	public Account(UUID owner) {
		this.owner = owner;

		for (Currency currency : ConfigManager.getConfig().getCurrencies()) {
			balances.put(currency, currency.getStartBalance());
		}
	}

	/**
	 *  Constructor for moving storage info into memory.
	 * @param account the StorageFormat of an account from storage.
	 */
	public Account(StorageFormat account) {
		this.owner = UUID.fromString(account.getOwner());

		HashMap<String, Float> storedBalances = account.getBalances();

		ArrayList<Currency> configCurrencies = ConfigManager.getConfig().getCurrencies();

		// Adds each currency to the new account.
		for (String currencyName : storedBalances.keySet()) {
			boolean found = false;

			for (Currency configCurrency : configCurrencies) {
				if (configCurrency.getName().equals(currencyName)) {
					balances.put(configCurrency, storedBalances.get(currencyName));
					found = true;
					break;
				}
			}

			// If the currency from storage can not be found in the config, add it to unavailable balances and log
			// that it couldn't be found.
			if (!found) {
				unavailableBalances.put(currencyName, storedBalances.get(currencyName));
				Multieconomy.LOGGER.error("Currency " + currencyName + " was not found in the config for UUID " +
					account.getOwner() + ".");
			}
		}

		boolean newCurrency = false;

		// If the player has no balance for a currency in the config, add it.
		for (Currency currency : configCurrencies) {
			if (!balances.containsKey(currency)) {
				balances.put(new Currency(currency.getName(), currency.getSingular(), currency.getPlural(),
						(int) currency.getStartBalance(), currency.isAllowPayments()), currency.getStartBalance());
				newCurrency = true;
			}
		}

		// If a new currency has been created for the user, save their new data to storage.
		if (newCurrency) {
			AccountManager.updateAccount(this);
		}
	}

	/**
	 * Getter for owner field.
	 * @return Account owners UUID
	 */
	public UUID getOwner() {
		return owner;
	}

	/**
	 * Getter for the owners balances.
	 * @return HashMap of Currency and the balance.
	 */
	public HashMap<Currency, Float> getBalances() {
		return balances;
	}

	/**
	 * Getter for the owners unavailable balances.
	 * @return HashMap of unavailable balances the player has.
	 */
	public HashMap<String, Float> getUnavailableBalances() {
		return unavailableBalances;
	}

	/**
	 * Getter for the balance of a single currency.
	 * @param currency The currency queried for the balance.
	 * @return float that represents the account owners balance.
	 */
	public float getBalance(Currency currency) {
		return balances.get(currency);
	}

	/**
	 *  Add method to add an amount to the currency supplied.
	 * @param currency The currency that should be added to.
	 * @param amount The amount that should be added.
	 * @return true if the balance was added successfully.
	 */
	public boolean add(Currency currency, float amount) {
		if (amount <= 0) {
			return false;
		}

		float oldAmount = balances.get(currency);
		balances.remove(currency);
		balances.put(currency, oldAmount + amount);
		return AccountManager.updateAccount(this);
	}

	/**
	 *  Method to remove money from a given currency.
	 * @param currency the currency to remove the amount from.
	 * @param amount the amount to remove.
	 * @return true if the amount was successfully removed.
	 */
	public boolean remove(Currency currency, float amount) {
		if (balances.get(currency) < amount || amount <= 0) {
			return false;
		}
		float oldAmount = balances.get(currency);
		balances.remove(currency);
		balances.put(currency, oldAmount - amount);
		return AccountManager.updateAccount(this);
	}

	/**
	 * Method to set the amount of money for a specified currency.
	 * @param currency the currency to set the balance of.
	 * @param amount the amount to set the currency to.
	 * @return true if the amount was successfully set.
	 */
	public boolean set(Currency currency, float amount) {
		if (amount < 0) {
			return false;
		}
		balances.remove(currency);
		balances.put(currency, amount);
		return AccountManager.updateAccount(this);
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
