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
package com.bencrow11.multieconomy.forge.events;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.events.PlayerJoinHandler;
import com.bencrow11.multieconomy.permission.PermissionManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Class to hold the event listener for when a player joins the server.
 */
public class PlayerJoinEvent {

	/**
	 *  Method to handle the player join event.
	 * @param event The Player Join Event
	 */
	@SubscribeEvent
	public void playerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
		new PlayerJoinHandler(event.getEntity().getName().getString(), event.getEntity().getUUID());

		// If the player has the "notify" perm, notify of any errors.
		if (PermissionManager.hasPermission(event.getEntity().getUUID(),
				PermissionManager.LOGIN_NOTIFY_PERMISSION)) {
			ErrorManager.printErrorsToPlayer(event.getEntity());
		}
	}

}
