/**
 * This file is part of MultiEconomy.
 *
 * MultiEconomy is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * MultiEconomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package com.bencrow11.multieconomy.command;

import com.bencrow11.multieconomy.command.commands.BalCommand;
import com.bencrow11.multieconomy.command.commands.BalTopCommand;
import com.bencrow11.multieconomy.command.commands.PayCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

/**
 * Abstract class to register all commands.
 */
public abstract class CommandRegistry {
	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register(BaseCommand::register);
		CommandRegistrationCallback.EVENT.register(PayCommand::register);
		CommandRegistrationCallback.EVENT.register(BalCommand::register);
		CommandRegistrationCallback.EVENT.register(BalTopCommand::register);
	}
}
