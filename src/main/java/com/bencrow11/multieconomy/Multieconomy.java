package com.bencrow11.multieconomy;

import com.bencrow11.multieconomy.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Multieconomy implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig();
	}
}
