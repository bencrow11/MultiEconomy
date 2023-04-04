package com.bencrow11.multieconomy.event;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.permission.PermissionManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

/**
 * Event listener that listens for when a player joins and checks they have an account.
 * If the player doesn't have an account, one is made for them.
 */
public class PlayerJoinHandler implements ServerPlayConnectionEvents.Join {
	@Override
	public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		// If the player doesn't have an account, create one.
		if (!AccountManager.hasAccount(handler.getPlayer().getUuid())) {
			AccountManager.createAccount(handler.getPlayer().getUuid());
		}


		if (PermissionManager.hasPermission(handler.getPlayer().getUuid(),
				PermissionManager.LOGIN_NOTIFY_PERMISSION)) {
			ErrorManager.printErrorsToPlayer(handler.getPlayer());
		}
	}
}
