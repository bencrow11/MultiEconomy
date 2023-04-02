package com.bencrow11.multieconomy.account;

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.storage.StorageManager;

import java.util.HashMap;
import java.util.UUID;

public abstract class AccountManager {
	private static HashMap<UUID, Account> accounts = new HashMap<>();

	public static boolean hasAccount(UUID player) {
		return accounts.containsKey(player);
	}

	public static Account getAccount(UUID player) {
		return accounts.get(player);
	}

	public static void updateAccount(Account account) {
		accounts.remove(account.getOwner());
		accounts.put(account.getOwner(), account);
		boolean success = StorageManager.writeAccount(account);

		if (!success) {
			Multieconomy.LOGGER.error("Failed to write account to storage for account: " + account.getOwner().toString());
		}
	}

	public static Account createAccount(UUID player) {
		accounts.put(player, new Account(player));
		boolean success = StorageManager.writeAccount(accounts.get(player));

		if (!success) {
			Multieconomy.LOGGER.error("Failed to write account to storage for account: " + player.toString());
		}
		return accounts.get(player);
	}

	public static void initialise(HashMap<UUID, Account> acc) {
		accounts = acc;
	}
}
