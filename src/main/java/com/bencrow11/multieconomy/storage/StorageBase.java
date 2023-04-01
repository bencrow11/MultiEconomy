package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public interface StorageBase {

	Account getAccount(ServerPlayerEntity player);

	boolean updateAccount(Account account);

	HashMap<ServerPlayerEntity, Account> getAll();
}
