package com.bencrow11.multieconomy.account;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.storage.StorageManager;

import java.util.HashMap;
import java.util.UUID;

/**
 * AccountManager class to store the accounts of all players.
 * Allows creation, updating and retrieving accounts.
 */
public abstract class AccountManager {
	private static HashMap<UUID, Account> accounts = new HashMap<>();

	/**
	 * Method to tell if the player has an account.
	 * @param player the UUID of the player to check.
	 * @return true if the player has an account.
	 */
	public static boolean hasAccount(UUID player) {
		return accounts.containsKey(player);
	}

	/**
	 * Method to retrieve the account of a player.
	 * @param player UUID of the players account to retrieve.
	 * @return the account of the player requested.
	 */
	public static Account getAccount(UUID player) {
		return accounts.get(player);
	}

	/**
	 * Method to update an account with the one provided.
	 * @param account the account that should be updated.
	 * @return true if the account was successfully updated.
	 */
	public static boolean updateAccount(Account account) {
		accounts.remove(account.getOwner());
		accounts.put(account.getOwner(), account);
		boolean success = StorageManager.writeAccount(account);

		if (!success) {
			ErrorManager.addError("Failed to write account to storage for account: " + account.getOwner().toString());
			return false;
		}

		return true;
	}

	/**
	 * Method to create a new account for a player.
	 * @param player the UUID of the player to create the account for.
	 * @return the create account.
	 */
	public static Account createAccount(UUID player) {
		accounts.put(player, new Account(player));
		boolean success = StorageManager.writeAccount(accounts.get(player));

		if (!success) {
			ErrorManager.addError("Failed to write account to storage for account: " + player.toString());
			return null;
		}
		return accounts.get(player);
	}

	/**
	 * Method to initialise the AccountManager when the server starts.
	 * @param acc the saved data from storage.
	 */
	public static void initialise(HashMap<UUID, Account> acc) {
		accounts = acc;
	}
}
