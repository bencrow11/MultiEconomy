package com.bencrow11.multieconomy.forge;

import dev.architectury.platform.forge.EventBuses;
import com.bencrow11.multieconomy.MultiEconomy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MultiEconomy.MOD_ID)
public class MultiEconomyForge {
	public MultiEconomyForge() {
		// Submit our event bus to let architectury register our content on the right time
		EventBuses.registerModEventBus(MultiEconomy.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		MultiEconomy.init();
	}
}