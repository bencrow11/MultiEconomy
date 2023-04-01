package com.bencrow11.multieconomy.account;

import com.bencrow11.multieconomy.storage.StorageManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AccountManager {
	private static HashMap<ServerPlayerEntity, Account> accounts = new HashMap<>();

	public static boolean hasAccount(ServerPlayerEntity player) {
		return accounts.containsKey(player);
	}

	public static Account getAccount(ServerPlayerEntity player) {
		return accounts.get(player);
	}

	public static void updateAccount(Account account) {
		accounts.remove(account.getOwner());
		accounts.put(account.getOwner(), account);
		StorageManager.writeAccount(account);
	}

	public static void createAccount(ServerPlayerEntity player) {
		accounts.put(player, new Account(player));
		StorageManager.writeAccount(accounts.get(player));
	}

	public static void initialise(HashMap<ServerPlayerEntity, Account> acc) {
		accounts = acc;
	}
}
