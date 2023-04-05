package com.bencrow11.multieconomy.event;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.permission.PermissionManager;
import com.bencrow11.multieconomy.player.Player;
import com.bencrow11.multieconomy.player.PlayerManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

/**
 * Event listener that listens for when a player joins and checks they have an account.
 * If the player doesn't have an account, one is made for them.
 */
public class PlayerJoinHandler implements ServerPlayConnectionEvents.Join {
	@Override
	public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {

		// If the player first joins, add their data.
		if (!PlayerManager.hasPlayer(handler.getPlayer().getUuid())) {
			PlayerManager.addPlayer(new Player(handler.getPlayer().getEntityName(), handler.getPlayer().getUuid()));
		}

		// If the player changes their name, update their data.
		if (!PlayerManager.getPlayerByUUID(handler.getPlayer().getUuid()).getName().equals(handler.getPlayer().getEntityName())) {
			PlayerManager.updatePlayer(new Player(handler.getPlayer().getEntityName(), handler.getPlayer().getUuid()));
		}


		// If the player doesn't have an account, create one.
		if (!AccountManager.hasAccount(handler.getPlayer().getUuid())) {
			AccountManager.createAccount(handler.getPlayer().getUuid());
		}

		// If the player has the "notify" perm, notify of any errors.
		if (PermissionManager.hasPermission(handler.getPlayer().getUuid(),
				PermissionManager.LOGIN_NOTIFY_PERMISSION)) {
			ErrorManager.printErrorsToPlayer(handler.getPlayer());
		}
	}
}
