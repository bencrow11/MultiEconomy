package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import com.bencrow11.multieconomy.account.AccountManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public abstract class StorageManager {

	private static final String path = new File("").getAbsolutePath() + "/config/MultiEconomy/accounts";

	public static boolean writeAccount(Account account) {
		try {
			File dir = checkForDirectory();

			String[] list = dir.list((dir1, name) -> name.equals(account.getOwner().toString() + ".json"));

			File file = new File(dir, account.getOwner().toString() + ".json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			if (list.length == 0) {
				file.createNewFile();
			}

			Writer writer = new FileWriter(file, false);

			StorageFormat storedAccount = new StorageFormat(account);

			gson.toJson(storedAccount, writer);
			writer.flush();
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Account readAccount(UUID player) {
		try {
			File dir = checkForDirectory();

			File file = new File(dir, player.toString() + ".json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			String[] list = dir.list((dir1, name) -> name.equals(player.toString() + ".json"));

			if (list.length == 0) {
				return AccountManager.createAccount(player);
			}

			Reader reader = new FileReader(file);

			StorageFormat playerAccount = gson.fromJson(reader, StorageFormat.class);

			reader.close();

			return new Account(playerAccount);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static HashMap<UUID, Account> getAllAccounts() {
		try {
			File dir = checkForDirectory();

			String[] list = dir.list();

			if (list.length == 0) {
				return new HashMap<>();
			}

			HashMap<UUID, Account> accounts = new HashMap<>();

			for (String item : list) {
				File file = new File(dir, item);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();

				Reader reader = new FileReader(file);

				StorageFormat playerAccount = gson.fromJson(reader, StorageFormat.class);

				reader.close();
				accounts.put(UUID.fromString(playerAccount.getOwner()), new Account(playerAccount));
			}

			return accounts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static File checkForDirectory() {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
}
