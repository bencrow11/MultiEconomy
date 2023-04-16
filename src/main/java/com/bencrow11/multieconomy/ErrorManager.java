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
package com.bencrow11.multieconomy;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;

public abstract class ErrorManager {
	private static ArrayList<String> errors = new ArrayList<>();

	public static void addError(String newError) {
		errors.add(newError);
	}

	public static ArrayList<String> getErrors() {
		return errors;
	}

	public static void printErrorsToPlayer(ServerPlayerEntity player) {
		if (errors.toArray().length == 0) {
			return;
		}

		String outputString = "§6MultiEconomy Errors: \n§c";

		for (String error : errors) {
			outputString = outputString + error.trim() + "\n";
		}

		player.sendMessage(Text.literal(outputString.trim()));
	}

	public static void printErrorsToConsole() {
		if (errors.toArray().length == 0) {
			return;
		}

		String outputString = "MultiEconomy Errors: \n";

		for (String error : errors) {
			outputString = outputString + error.trim() + "\n";
		}

		Multieconomy.LOGGER.error(outputString.trim());
	}
}
