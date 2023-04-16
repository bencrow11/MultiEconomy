/**
 * This file is part of MultiEconomy.
 *
 * MultiEconomy is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * MultiEconomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package com.bencrow11.multieconomy;

import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.command.CommandRegistry;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.event.PlayerJoinHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Multieconomy implements ModInitializer {

	public static final String VERSION = "1.0.0";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String BASE_COMMAND = "multieconomy";
	public static final String[] ALIASES = {"multieco", "meco"};

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig(); // Loads the config from file.
		AccountManager.initialise(); // Adds saved accounts to memory.
		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinHandler()); // Registers PlayerJoin event handler.
		CommandRegistry.registerCommands(); // Registers the commands.
		LOGGER.info("MultiEconomy Loaded.");
		ErrorManager.printErrorsToConsole();
	}
}
