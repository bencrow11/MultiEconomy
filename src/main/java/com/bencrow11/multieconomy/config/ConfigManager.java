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
package com.bencrow11.multieconomy.config;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.util.Utils;

/**
 * Manager class to load, write and store the config.
 */
public abstract class ConfigManager {
	// The field that will store the config.
	private static Config multiEcoConfig;

	/**
	 * Method to load the config and store it into the multiEcoConfig field.
	 */
	public static void loadConfig() {

		Config cfg = null;

		cfg = Utils.readFromFile("", "config", Config.class);

		if (cfg == null) {
			Multieconomy.LOGGER.info("Couldn't find config for MultiEconomy, creating new config.");
			boolean success = Utils.writeToFile("", "config", new Config());

			if (!success) {
				ErrorManager.addError("Failed to create config. Please check the console for any errors.");
				return;
			}

			cfg = Utils.readFromFile("", "config", Config.class);
		}


			boolean hasPassed = Utils.checkConfig(cfg);

			if (!hasPassed) {
				ErrorManager.addError("Config is invalid. Please regenerate or fix the errors.");
			}

			multiEcoConfig = cfg;
	}

	public static Config getConfig() {
		return ConfigManager.multiEcoConfig;
	}

}
