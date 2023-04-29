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
package com.bencrow11.multieconomy.fabric.events;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.account.Account;
import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.permission.PermissionManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * Event listener that listens for when a player joins and checks they have an account.
 * If the player doesn't have an account, one is made for them.
 */
public class PlayerJoinHandler implements ServerPlayConnectionEvents.Join {
	@Override
	public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {

		// If the player has changed their name, update it.
		if (!AccountManager.hasAccount(handler.getPlayer().getName().getString()) &&
				AccountManager.hasAccount(handler.getPlayer().getName().getString())) {
			Account account = AccountManager.getAccount(handler.getPlayer().getUUID());
			account.changeUsername(handler.getPlayer().getName().getString());
			AccountManager.updateAccount(account);
		}

		// If the player doesn't have an account, create one.
		if (!AccountManager.hasAccount(handler.getPlayer().getUUID())) {
			AccountManager.createAccount(handler.getPlayer().getUUID(), handler.getPlayer().getName().getString());
		}

		// If the player has the "notify" perm, notify of any errors.
		if (PermissionManager.hasPermission(handler.getPlayer().getUUID(),
				PermissionManager.LOGIN_NOTIFY_PERMISSION)) {
			ErrorManager.printErrorsToPlayer(handler.getPlayer());
		}
	}
}