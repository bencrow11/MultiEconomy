package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

// Class that stores SQL connection data
public class StorageSQL implements StorageBase {

	private final String database;
	private final String hostname;
	private final String password;
	private final String port;
	private final String username;

	public StorageSQL(String database, String hostname, String password, String port, String username) {
		this.database = database;
		this.hostname = hostname;
		this.password = password;
		this.port = port;
		this.username = username;
	}

	@Override
	public Account getAccount(PlayerEntity player) {
		return null;
	}

	@Override
	public boolean updateAccount(Account account) {
		return false;
	}

	@Override
	public HashMap<PlayerEntity, Account> getAll() {
		return null;
	}
}
