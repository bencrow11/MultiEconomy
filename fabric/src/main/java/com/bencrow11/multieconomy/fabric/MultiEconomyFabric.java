package com.bencrow11.multieconomy.fabric;

import com.bencrow11.multieconomy.MultiEconomy;
import com.bencrow11.multieconomy.fabric.events.PlayerJoinHandler;
import com.bencrow11.multieconomy.fabric.registry.CommandRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class MultiEconomyFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		MultiEconomy.init();
		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinHandler()); // Registers PlayerJoin event handler.
		CommandRegistry.registerCommands(); // Registers the commands.
	}
}