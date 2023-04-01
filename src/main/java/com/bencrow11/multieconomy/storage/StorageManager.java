package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import com.bencrow11.multieconomy.config.ConfigManager;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public abstract class StorageManager {

	private static boolean isDatabase = ConfigManager.getConfig().isUseSQL();
	private static StorageBase storage = isDatabase ? ConfigManager.getConfig().getDatabaseConnection() :
			new StorageJSON("/config/MultiEconomy/Accounts");

	public static void writeAccount(Account account) {
		storage.updateAccount(account);
	}

	public static Account readAccount(PlayerEntity player) {
		return storage.getAccount(player);
	}

	public static HashMap<PlayerEntity, Account> getAllAccounts() {
		return storage.getAll();
	}
}
