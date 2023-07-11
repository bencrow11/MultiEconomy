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
package com.bencrow11.multieconomy.placeholder;

import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;

import java.util.UUID;

/**
 * Class used to grab placeholder values.
 */
public abstract class Placeholders {

	/**
	 * Method to get the default balance of the player.
	 * @param player The player to get the balance of.
	 * @return float that represents the value of the players balance.
	 */
	public static float getDefaultBalance(UUID player) {
		Currency defaultCurrency = ConfigManager.getConfig().getCurrencyByName(ConfigManager.getConfig().getDefaultCurrency());

		return AccountManager.getAccount(player).getBalance(defaultCurrency);
	}

	/**
	 * Method to get the balance of the player and currency given.
	 * @param player The player to get the balance of.
	 * @param currency The currency to get the balance of.
	 * @return float that represents the currency of the player.
	 */
	public static float getBalance(UUID player, Currency currency) {
		return AccountManager.getAccount(player).getBalance(currency);
	}


}
