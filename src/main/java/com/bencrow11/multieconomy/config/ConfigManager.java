package com.bencrow11.multieconomy.config;

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.currency.Currency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;

/**
 * Manager class to load, write and store the config.
 */
public abstract class ConfigManager {
	// The field that will store the config.
	private static Config multiEcoConfig;

	/**
	 * Method to load the config and store it into the multiEcoConfig field.
	 * @return true if the config loaded correctly.
	 */
	public static boolean loadConfig() {

		String root = new File("").getAbsolutePath();
		String path = root + "/config/MultiEconomy";

		try {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String[] list = dir.list((dir1, name) -> name.equalsIgnoreCase("config.json"));

			File file = new File(dir, "config.json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			if (list.length == 0) {
				createConfigFile(path);
			}

			Reader reader = new FileReader(file);
			ConfigManager.multiEcoConfig = gson.fromJson(reader, Config.class);
			reader.close();

			checkConfig();

			return true;

		} catch (Exception e) {
			Multieconomy.LOGGER.fatal("MultiEconomy config could not be loaded properly. Please check the config or " +
					"generate a new one.");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method to create a new config file.
	 * @param path the filepath that should be used.
	 */
	public static void createConfigFile(String path){
		File dir = new File(path);
		File file = new File(dir, "config.json");

		try {
			file.createNewFile();
			Writer writer = new FileWriter(file, false);

			ArrayList<Currency> defaultCurrencies = new ArrayList<>();
			defaultCurrencies.add(new Currency("dollars", "dollar", "dollars", 100, true));

			Config defaultConfig = new Config(true, "dollars", defaultCurrencies);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(defaultConfig, writer);
			writer.flush();
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to check the config is valid.
	 * @return true if the config is valid.
	 */
	public static boolean checkConfig(){
		ArrayList<Currency> currencies = ConfigManager.multiEcoConfig.getCurrencies();
		String defaultCurrency = ConfigManager.multiEcoConfig.getDefaultCurrency().trim().toLowerCase();

		for (Currency currency : currencies) {
			String currencyFormatted = currency.getName().trim().toLowerCase();
			if (currencyFormatted.equals(defaultCurrency)) {
				return true;
			}
		}
		Multieconomy.LOGGER.fatal("Multicurrency default currency " + ConfigManager.multiEcoConfig.getDefaultCurrency() +
				" doesn't match any existing currency name.");

		for (int i = 0; i < currencies.toArray().length; i++) {
			String currentCurrency = currencies.get(i).getName();

			for (int x = i + 1; x < currencies.toArray().length; x++) {
				String comparedCurrency = currencies.get(x).getName();
				if (currentCurrency.equals(comparedCurrency)) {
					Multieconomy.LOGGER.fatal("Found duplicate currency with name: " + currentCurrency);
					return true;
				}
			}
		}

		return false;
	}

	public static Config getConfig() {
		return ConfigManager.multiEcoConfig;
	}

}
