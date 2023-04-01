package com.bencrow11.multieconomy.account;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public abstract class AccountManager {
	private static HashMap<PlayerEntity, Account> accounts;


	public static Account getAccount(PlayerEntity player) {
		return accounts.get(player);
	}

	public static void updateAccount(Account account) {

	}

	public static void createAccount(PlayerEntity player) {
		accounts.put(player, new Account(player));
	}

	public static void initialise(HashMap<PlayerEntity, Account> acc) {
		accounts = acc;
	}
}
