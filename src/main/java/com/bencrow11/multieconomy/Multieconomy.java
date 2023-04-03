package com.bencrow11.multieconomy;

import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.event.PlayerJoinHandler;
import com.bencrow11.multieconomy.storage.StorageManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class Multieconomy implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig(); // Loads the config from file.
		AccountManager.initialise(StorageManager.getAllAccounts()); // Adds saved accounts to memory.
		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinHandler()); // Registers PlayerJoin event handler.
		AccountManager.updateAccount(AccountManager.getAccount(UUID.fromString("4139d0ef-81e0-4a23-b7cd-d850ced3b55a")));
	}

}
