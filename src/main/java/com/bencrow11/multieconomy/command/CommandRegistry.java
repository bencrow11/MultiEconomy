package com.bencrow11.multieconomy.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public abstract class CommandRegistry {
	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register(BaseCommand::register);
	}
}
