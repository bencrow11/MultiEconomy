package com.bencrow11.multieconomy.forge.events;

import com.bencrow11.multieconomy.command.BaseCommand;
import com.bencrow11.multieconomy.command.commands.BalCommand;
import com.bencrow11.multieconomy.command.commands.BalTopCommand;
import com.bencrow11.multieconomy.command.commands.PayCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterCommandEvent {

	@SubscribeEvent
	public void registerCommands(RegisterCommandsEvent event) {
		BalCommand.register(event.getDispatcher());
		BalTopCommand.register(event.getDispatcher());
		PayCommand.register(event.getDispatcher());
		BaseCommand.register(event.getDispatcher());
	}
}
