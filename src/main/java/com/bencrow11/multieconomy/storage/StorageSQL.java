package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import net.minecraft.server.network.ServerPlayerEntity;

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
	public Account getAccount(ServerPlayerEntity player) {
		return null;
	}

	@Override
	public boolean updateAccount(Account account) {
		return false;
	}

	@Override
	public HashMap<ServerPlayerEntity, Account> getAll() {
		return null;
	}
}
