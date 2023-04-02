package com.bencrow11.multieconomy.event;

import com.bencrow11.multieconomy.account.AccountManager;
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
		// If the player doesn't have an account, create one.
		if (!AccountManager.hasAccount(handler.getPlayer().getUuid())) {
			AccountManager.createAccount(handler.getPlayer().getUuid());
		}
	}
}
