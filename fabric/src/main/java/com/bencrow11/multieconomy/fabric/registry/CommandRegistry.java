package com.bencrow11.multieconomy.fabric.registry;

import com.bencrow11.multieconomy.command.BaseCommand;
import com.bencrow11.multieconomy.command.commands.BalCommand;
import com.bencrow11.multieconomy.command.commands.BalTopCommand;
import com.bencrow11.multieconomy.command.commands.PayCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public abstract class CommandRegistry {
	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register(BaseCommand::register);
		CommandRegistrationCallback.EVENT.register(PayCommand::register);
		CommandRegistrationCallback.EVENT.register(BalCommand::register);
		CommandRegistrationCallback.EVENT.register(BalTopCommand::register);
	}
}