package com.bencrow11.multieconomy.util;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.config.Config;
import com.bencrow11.multieconomy.config.ConfigManager;
import com.bencrow11.multieconomy.currency.Currency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.io.*;
import java.util.ArrayList;

public class Utils {

	private static final String BASE_PATH = new File("").getAbsolutePath() + "/config/MultiEconomy/";

	public static <T> T readFromFile(String subpath, String filename, Class<T> dataType) {
		try {
			File dir = checkForDirectory(BASE_PATH + subpath);

			String[] list = findFileName(dir, filename + ".json");

			if (list.length == 0) {
				return null;
			}

			File file = new File(dir, filename + ".json");
			Gson gson = newGson();

			Reader reader = new FileReader(file);

			T data = gson.fromJson(reader, dataType);

			reader.close();

			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> ArrayList<T> getAllFiles(String subpath, Class<T> dataType) {
		try {
			File dir = checkForDirectory(BASE_PATH + subpath);

			String[] list = dir.list();

			if (list.length == 0) {
				return null;
			}

			ArrayList<T> files = new ArrayList<>();

			for (String item : list) {
				File file = new File(dir, item);
				Gson gson = newGson();

				Reader reader = new FileReader(file);

				T data = gson.fromJson(reader, dataType);
				reader.close();

				files.add(data);
			}

			return files;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean writeToFile(String subpath, String filename, Object data) {
		try {
			File dir = checkForDirectory(BASE_PATH + subpath);

			String[] list = findFileName(dir, filename + ".json");

			File file = new File(dir, filename + ".json");
			Gson gson = newGson();

			if (list.length == 0) {
				file.createNewFile();
			}

			Writer writer = new FileWriter(file, false);

			gson.toJson(data, writer);
			writer.flush();
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static File checkForDirectory(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	private static String[] findFileName(File dir, String filename) {
		return dir.list((dir1, name) -> name.equals(filename));
	}

	private static Gson newGson() {
		return new GsonBuilder().setPrettyPrinting().create();
	}

	/**
	 * Method to check the config is valid.
	 * @return true if the config is valid.
	 */
	public static boolean checkConfig(Config cfg){
		ArrayList<Currency> currencies = cfg.getCurrencies();
		String defaultCurrency = cfg.getDefaultCurrency().trim().toLowerCase();

		for (int i = 0; i < currencies.toArray().length; i++) {
			String currentCurrency = currencies.get(i).getName();

			for (int x = i + 1; x < currencies.toArray().length; x++) {
				String comparedCurrency = currencies.get(x).getName();
				if (currentCurrency.equals(comparedCurrency)) {
					ErrorManager.addError("Found duplicate currency with name: " + currentCurrency);
					return false;
				}
			}
		}

		for (Currency currency : currencies) {
			String currencyFormatted = currency.getName().trim().toLowerCase();
			if (currencyFormatted.equals(defaultCurrency)) {
				return true;
			}
		}
		ErrorManager.addError("Multicurrency default currency " + cfg.getDefaultCurrency() +
				" doesn't match any existing currency name.");

		return false;
	}

	public static void registerAliases(CommandDispatcher<ServerCommandSource> dispatcher,
	                                   LiteralCommandNode<ServerCommandSource> command) {
		for (String alias : Multieconomy.ALIASES) {
			dispatcher.register(CommandManager.literal(alias).redirect(command));
		}
	}
}
