package com.bencrow11.multieconomy.config;

import com.bencrow11.multieconomy.currency.Currency;

import java.util.ArrayList;

/**
 * Class that represents the config to create and store the values.
 */
public class Config {
	// Whether the game should allow players to pay offline.
	private final boolean allowOfflinePayments;
	// The default currency for players to use.
	private final String defaultCurrency;
	// All the currencies available.
	private final ArrayList<Currency> currencies;

	/**
	 * Constructor the create a new config.
	 * @param allowOfflinePayments sets the field allowOfflinePayments.
	 * @param defaultCurrency sets the field defaultCurrency.
	 * @param currencies sets the field currencies.
	 */
	public Config(boolean allowOfflinePayments, String defaultCurrency,
	              ArrayList<Currency> currencies) {
		this.allowOfflinePayments = allowOfflinePayments;
		this.defaultCurrency = defaultCurrency;
		this.currencies = currencies;
	}

	/**
	 * Getter for allowOfflinePayments.
	 * @return true if offline payments are allowed.
	 */
	public boolean isAllowOfflinePayments() {
		return allowOfflinePayments;
	}

	/**
	 * Getter for defaultCurrency
	 * @return the name of the default currency.
	 */
	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	/**
	 * Getter for currencies.
	 * @return ArrayList of all the currencies available.
	 */
	public ArrayList<Currency> getCurrencies() {
		return currencies;
	}

	/**
	 * Getter for currency names.
	 * @return ArrayList of all the currency names available.
	 */
	public ArrayList<String> getCurrenciesAsString() {
		ArrayList<String> strings = new ArrayList<>();

		for (Currency currency : currencies) {
			strings.add(currency.getName());
		}

		return strings;
	}

	public Currency getCurrencyByName(String currencyName) {
		for (Currency currency : currencies) {
			if (currency.getName().toLowerCase().trim().equals(currencyName.toLowerCase().trim())) {
				return currency;
			}
		}
		return null;
	}
}
