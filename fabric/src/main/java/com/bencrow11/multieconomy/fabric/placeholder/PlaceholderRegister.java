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
package com.bencrow11.multieconomy.fabric.placeholder;

import com.bencrow11.multieconomy.MultiEconomy;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.resources.ResourceLocation;

/**
 * Registry for Placeholders on Fabric
 */
public abstract class PlaceholderRegister {
	/**
	 * Method to register all placeholders.
	 */
	public static void registerPlaceholders() {

		// Register all placeholders for currencies "multieconomy:currency_[currency name]"
		for (Currency currency : ConfigManager.getConfig().getCurrencies()) {
			Placeholders.register(ResourceLocation.tryBuild(MultiEconomy.MOD_ID, "currency_" + currency.getName()),
					(ctx, arg) -> {
					if (!ctx.hasPlayer()) {
						return PlaceholderResult.invalid("No Player!");
					}
					float balance = com.bencrow11.multieconomy.placeholder.Placeholders.getBalance(ctx.player().getUUID(), currency);

					return PlaceholderResult.value(String.valueOf(balance));
			});
		}

		// Register placeholder for the default currency "multieconomy:currency"
		Placeholders.register(ResourceLocation.tryBuild(MultiEconomy.MOD_ID, "currency"),
				(ctx, arg) -> {
					if (!ctx.hasPlayer()) {
						return PlaceholderResult.invalid("No Player!");
					}
					float balance = com.bencrow11.multieconomy.placeholder.Placeholders.getDefaultBalance(ctx.player().getUUID());

					return PlaceholderResult.value(String.valueOf(balance));
				});
	}

}
