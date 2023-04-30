package com.bencrow11.multieconomy.events;

import com.bencrow11.multieconomy.account.Account;
import com.bencrow11.multieconomy.account.AccountManager;

import java.util.UUID;

public class PlayerJoinHandler {

	public PlayerJoinHandler(String username, UUID uuid) {
		// If the player has changed their name, update it.
		if (!AccountManager.hasAccount(username) &&
				AccountManager.hasAccount(username)) {
			Account account = AccountManager.getAccount(uuid);
			account.changeUsername(username);
			AccountManager.updateAccount(account);
		}

		// If the player doesn't have an account, create one.
		if (!AccountManager.hasAccount(uuid)) {
			AccountManager.createAccount(uuid, username);
		}
	}
}
