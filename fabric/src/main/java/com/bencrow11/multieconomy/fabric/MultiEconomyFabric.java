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
package com.bencrow11.multieconomy.fabric;

import com.bencrow11.multieconomy.MultiEconomy;
import com.bencrow11.multieconomy.fabric.events.PlayerJoinEvent;
import com.bencrow11.multieconomy.fabric.registry.CommandRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class MultiEconomyFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		MultiEconomy.init();
		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinEvent()); // Registers PlayerJoin event handler.
		CommandRegistry.registerCommands(); // Registers the commands.
	}
}