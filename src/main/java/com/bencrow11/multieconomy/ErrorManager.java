package com.bencrow11.multieconomy;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.UUID;

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

		String outputString = "&4MultiEconomy Errors: \n";

		for (String error : errors) {
			outputString = outputString + error.trim() + "\n";
		}

		player.sendMessage(Text.literal(outputString));
	}
}
