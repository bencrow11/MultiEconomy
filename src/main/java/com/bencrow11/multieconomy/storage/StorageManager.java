package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import com.bencrow11.multieconomy.config.ConfigManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public abstract class StorageManager {

	private static boolean isDatabase = ConfigManager.getConfig().isUseSQL();
	private static StorageBase storage = isDatabase ? ConfigManager.getConfig().getDatabaseConnection() :
			new StorageJSON("/config/MultiEconomy/Accounts");

	public static void writeAccount(Account account) {
		storage.updateAccount(account);
	}

	public static Account readAccount(ServerPlayerEntity player) {
		return storage.getAccount(player);
	}

	public static HashMap<ServerPlayerEntity, Account> getAllAccounts() {
		return storage.getAll();
	}
}
