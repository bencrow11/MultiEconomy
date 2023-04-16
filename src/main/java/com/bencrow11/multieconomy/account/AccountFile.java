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
package com.bencrow11.multieconomy.account;

import com.bencrow11.multieconomy.currency.Currency;

import java.util.HashMap;

/**
 * Class that is used to convert an Account to the correct format for saving to file.
 * This is only used for reading / writing to file.
 */
public class AccountFile {
	private final String uuid;
	private final String username;
	private HashMap<String, Float> balances = new HashMap<>();

	/**
	 * Constructor that takes an account and converts it to the correct format
	 * for the class.
	 * @param account A users account.
	 */
	public AccountFile(Account account) {
		this.uuid = account.getUUID().toString();
		this.username = account.getUsername();

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
