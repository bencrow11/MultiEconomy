package com.bencrow11.multieconomy.config;

import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.storage.StorageSQL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.math.ColorHelper;

import java.io.*;
import java.util.ArrayList;

public abstract class ConfigManager {
	static Config multiEcoConfig;

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
			e.printStackTrace();
			return false;
		}
	}

	public static void createConfigFile(String path){
		File dir = new File(path);
		File file = new File(dir, "config.json");

		try {
			file.createNewFile();
			Writer writer = new FileWriter(file, false);

			ArrayList<Currency> defaultCurrencies = new ArrayList<>();
			defaultCurrencies.add(new Currency("dollars", "dollar", "dollars", 100, true));

			Config defaultConfig = new Config(true, false,
					new StorageSQL("database", "hostname", "password", "3306", "username"),
					"dollars", defaultCurrencies);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(defaultConfig, writer);
			writer.flush();
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean checkConfig(){
		ArrayList<Currency> currencies = ConfigManager.multiEcoConfig.getCurrencies();
		String defaultCurrency = ConfigManager.multiEcoConfig.getDefaultCurrency().trim().toLowerCase();

		for (Currency currency : currencies) {
			String currencyFormatted = currency.getName().trim().toLowerCase();
			if (currencyFormatted.equals(defaultCurrency)) {
				return true;
			}
		}
		Multieconomy.LOGGER.error("Multicurrency default currency is missing or doesn't match any existing currencies.");
		return false;
	}
}
