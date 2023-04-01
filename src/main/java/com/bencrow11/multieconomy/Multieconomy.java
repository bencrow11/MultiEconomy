package com.bencrow11.multieconomy;

import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.event.PlayerJoinHandler;
import com.bencrow11.multieconomy.storage.StorageManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Multieconomy implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig();
//		AccountManager.initialise(StorageManager.getAllAccounts());
		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinHandler());
	}

}
