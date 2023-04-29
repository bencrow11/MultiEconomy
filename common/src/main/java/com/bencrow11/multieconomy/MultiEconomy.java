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
import com.bencrow11.multieconomy.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point to the mod.
 */
public class MultiEconomy {

	public static final String MOD_ID = "multieconomy";

	public static final String VERSION = "1.0.0"; // Mod version.
	public static final Logger LOGGER = LogManager.getLogger(); // Logger for logging to console.
	public static final String BASE_COMMAND = "multieconomy"; // Base command for the mod.
	public static final String[] ALIASES = {"multieco", "meco"}; // Aliases of the base command.

	/**
	 * Initializes the mod.
	 */
	public static void init() {
		ConfigManager.loadConfig(); // Loads the config from file.
		AccountManager.initialise(); // Adds saved accounts to memory.
		LOGGER.info("MultiEconomy Loaded.");
		ErrorManager.printErrorsToConsole();
	}
}
