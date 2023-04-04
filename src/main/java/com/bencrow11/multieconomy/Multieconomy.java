package com.bencrow11.multieconomy;

import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.command.CommandUtils;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.event.PlayerJoinHandler;
import com.bencrow11.multieconomy.storage.StorageManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Multieconomy implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String BASE_COMMAND = "multieconomy";
	public static final String[] ALIASES = {"multieco", "meco"};

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig(); // Loads the config from file.
		AccountManager.initialise(StorageManager.getAllAccounts()); // Adds saved accounts to memory.
		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinHandler()); // Registers PlayerJoin event handler.
		CommandUtils.registerCommands(); // Registers the commands.
		LOGGER.info("MultiEconomy Loaded.");
		ErrorManager.printErrorsToConsole();
	}

}
