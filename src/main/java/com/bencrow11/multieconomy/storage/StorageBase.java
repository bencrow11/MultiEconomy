package com.bencrow11.multieconomy.storage;

import com.bencrow11.multieconomy.account.Account;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public interface StorageBase {

	Account getAccount(PlayerEntity player);

	boolean updateAccount(Account account);

	HashMap<PlayerEntity, Account> getAll();
}
