package com.bencrow11.multieconomy.account;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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
		return accounts.containsKey(username.toLowerCase());
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
		return accounts.get(username.toLowerCase());
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
	 * Method to update an account with the one provided.
	 * @param account the account that should be updated.
	 * @return true if the account was successfully updated.
	 */
	public static boolean updateAccount(Account account) {
		accounts.remove(account.getUsername().toLowerCase());
		accounts.put(account.getUsername().toLowerCase(), account);
		boolean success = Utils.writeToFile("accounts", account.getUUID().toString(), new AccountFile(account));

		if (!success) {
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
		accounts.put(username.toLowerCase(), new Account(uuid, username));
		boolean success = Utils.writeToFile("accounts", accounts.get(username.toLowerCase()).getUUID().toString(),
				new AccountFile(accounts.get(username.toLowerCase())));

		if (!success) {
			ErrorManager.addError("Failed to write account to storage for account: " + username);
			Multieconomy.LOGGER.error("Failed to write account to storage for account: " + username);
			return false;
		}
		return true;
	}

	/**
	 * Method to initialise the AccountManager when the server starts.
	 */
	public static void initialise() {
		ArrayList<AccountFile> accountFiles = Utils.getAllFiles("accounts", AccountFile.class);

		if (accountFiles == null) {
			return;
		}

		for (AccountFile file : accountFiles) {
			Account convertedAccount = new Account(file);
			accounts.put(convertedAccount.getUsername().toLowerCase(), convertedAccount);
		}
	}
}
