/**
 * This file is part of MultiEconomy.
 *
 * MultiEconomy is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * MultiEconomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
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
	 * Constructor the create a new config with default values.
	 */
	public Config() {
		this.allowOfflinePayments = true;
		this.defaultCurrency = "dollar";

		ArrayList<Currency> defaultCurrencies = new ArrayList<>();
		defaultCurrencies.add(new Currency("dollar", "dollar", "dollars", 100, true));

		this.currencies = defaultCurrencies;
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

	/**
	 * Method to get a currency by it's name.
	 * @param currencyName The name of the currency.
	 * @return The currency if it exists. Otherwise null.
	 */
	public Currency getCurrencyByName(String currencyName) {
		for (Currency currency : currencies) {
			if (currency.getName().trim().equalsIgnoreCase(currencyName.trim())) {
				return currency;
			}
		}
		return null;
	}
}
