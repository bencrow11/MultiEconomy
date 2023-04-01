package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class StorageJSON implements StorageBase {

	private final String filepath;

	public StorageJSON(String filepath) {
		this.filepath = filepath;
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
