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
package com.bencrow11.multieconomy.forge;

import com.bencrow11.multieconomy.MultiEconomy;
import com.bencrow11.multieconomy.forge.events.PlayerJoinEvent;
import com.bencrow11.multieconomy.forge.events.RegisterCommandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(MultiEconomy.MOD_ID)
public class MultiEconomyForge {
	public MultiEconomyForge() {
		MultiEconomy.init();
		MinecraftForge.EVENT_BUS.register(new PlayerJoinEvent()); // Registers PlayerJoin event handler.
		MinecraftForge.EVENT_BUS.register(new RegisterCommandEvent()); // Registers PlayerJoin event handler.
	}
}