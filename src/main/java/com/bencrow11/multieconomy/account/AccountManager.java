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

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.util.Utils;
import com.google.gson.Gson;

import java.io.File;
import java.util.*;

import static com.bencrow11.multieconomy.util.Utils.readFileAsync;

/**
 * AccountManager class to store the accounts of all players.
 * Allows creation, updating and retrieving accounts.
 */
public abstract class AccountManager {
	private static HashMap<String, Account> accounts = new HashMap<>();

	/**
	 * Method to tell if the player has an account.
	 * @param username the username of the player to check.
	 * @return true if the player has an account.
	 */
	public static boolean hasAccount(String username) {
		for (String name : accounts.keySet()) {
			if (name.equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasAccount(UUID uuid) {
		for (Account account : accounts.values()) {
			if (account.getUUID().equals(uuid)) return true;
		}
		return false;
	}

	/**
	 * Method to retrieve the account of a player.
	 * @param username username of the players account to retrieve.
	 * @return the account of the player requested.
	 */
	public static Account getAccount(String username) {
		for (String name : accounts.keySet()) {
			if (name.equalsIgnoreCase(username)) {
				return accounts.get(name);
			}
		}
		return null;
	}

	/**
	 * Gets the account of a player.
	 * @param uuid uuid of the players account to retrieve.
	 * @return the account of the player.
	 */
	public static Account getAccount(UUID uuid) {
		for (Account account : accounts.values()) {
			if (account.getUUID().equals(uuid)) return account;
		}
		return null;
	}

	/**
	 * Method to get all accounts in memory.
	 * @return ArrayList that holds all players accounts.
	 */
	public static ArrayList<Account> getAllAccounts() {
		return (ArrayList<Account>) accounts.values();
	}


	/**
	 * Method to update an account with the one provided.
	 * @param account the account that should be updated.
	 * @return true if the account was successfully updated.
	 */
	public static boolean updateAccount(Account account) {
		Account oldAccount = accounts.get(account.getUsername());
		accounts.remove(account.getUsername());
		accounts.put(account.getUsername(), account);
		Gson gson = Utils.newGson();
		boolean success = Utils.writeFileAsync("accounts/", account.getUUID().toString() + ".json",
				 gson.toJson(new AccountFile(account)));
		if (!success) {
			accounts.remove(account.getUsername());
			accounts.put(account.getUsername(), oldAccount);
			ErrorManager.addError("Failed to write account to storage for account: " + account.getUsername());
			Multieconomy.LOGGER.error("Failed to write account to storage for account: " + account.getUsername());
			return false;
		}

		return true;
	}

	/**
	 * Method to create a new account for a player.
	 * @param uuid the UUID of the player to create the account for.
	 * @param username the username of the player.
	 * @return the create account.
	 */
	public static boolean createAccount(UUID uuid, String username) {
		accounts.put(username, new Account(uuid, username));
		Gson gson = Utils.newGson();
		boolean success = Utils.writeFileAsync("accounts/", getAccount(username).getUUID().toString() + ".json",
				gson.toJson(new AccountFile(accounts.get(username))));
		if (!success) {
			ErrorManager.addError("Failed to write account to storage for account: " + username);
			Multieconomy.LOGGER.error("Failed to write account to storage for account: " + username);
			return false;
		}
		return true;
	}

	/**
	 * Gets all sorted account balances of a given currency.
	 * @param currency the currency to sort by.
	 * @return ArrayList that holds a lists containing a username and balance.
	 */
	public static List<Account> sortAccountsByBalance(Currency currency) {

		Comparator<Account> currencyComparator = ((ac1, ac2) -> Float.compare(ac1.getBalance(currency), ac2.getBalance(currency)));

		List<Account> sortedAccounts = accounts.values().
				stream().
				sorted(currencyComparator.reversed()).toList();

		return sortedAccounts;
	}


	/**
	 * Method to initialise the AccountManager when the server starts.
	 */
	public static void initialise() {
		try {
			File dir = Utils.checkForDirectory(Utils.BASE_PATH + "accounts/");

			String[] list = dir.list();

			if (list.length == 0) {
				return;
			}

			for (String file : list) {
				readFileAsync("accounts/", file, (el -> {
					Gson gson = Utils.newGson();
					AccountFile accountFile = gson.fromJson(el, AccountFile.class);
					accounts.put(accountFile.getUsername(), new Account(accountFile));
				}));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
