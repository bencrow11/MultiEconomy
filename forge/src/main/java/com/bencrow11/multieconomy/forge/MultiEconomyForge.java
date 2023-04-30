package com.bencrow11.multieconomy.forge;

import com.bencrow11.multieconomy.MultiEconomy;
import com.bencrow11.multieconomy.forge.events.PlayerJoinEvent;
import com.bencrow11.multieconomy.forge.events.RegisterCommandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(MultiEconomy.MOD_ID)
public class MultiEconomyForge {
	public MultiEconomyForge() {
//		// Submit our event bus to let architectury register our content on the right time
//		MinecraftForge.EVENT_BUS.register(this);
		MultiEconomy.init();
		MinecraftForge.EVENT_BUS.register(new PlayerJoinEvent()); // Registers PlayerJoin event handler.
		MinecraftForge.EVENT_BUS.register(new RegisterCommandEvent()); // Registers PlayerJoin event handler.
	}
}