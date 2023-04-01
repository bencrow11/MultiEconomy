package com.bencrow11.multieconomy.event;

import com.bencrow11.multieconomy.account.AccountManager;
import com.bencrow11.multieconomy.currency.Currency;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;

import java.util.Set;

public class PlayerJoinHandler implements ServerPlayConnectionEvents.Join {
	@Override
	public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		if (!AccountManager.hasAccount(handler.getPlayer())) {
			AccountManager.createAccount(handler.getPlayer());
		}
	}
}
