package com.bencrow11.multieconomy.config;

import com.bencrow11.multieconomy.currency.Currency;
import com.bencrow11.multieconomy.storage.StorageSQL;

import java.util.ArrayList;

public class Config {
	private final boolean allowOfflinePayments;
	private final boolean useSQL;
	private final StorageSQL databaseConnection;
	private final String defaultCurrency;
	private final ArrayList<Currency> currencies;

	public Config(boolean allowOfflinePayments, boolean useSQL, StorageSQL databaseConnection, String defaultCurrency,
	              ArrayList<Currency> currencies) {
		this.allowOfflinePayments = allowOfflinePayments;
		this.useSQL = useSQL;
		this.databaseConnection = databaseConnection;
		this.defaultCurrency = defaultCurrency;
		this.currencies = currencies;
	}

	public boolean isAllowOfflinePayments() {
		return allowOfflinePayments;
	}

	public boolean isUseSQL() {
		return useSQL;
	}

	public StorageSQL getDatabaseConnection() {
		return databaseConnection;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public ArrayList<Currency> getCurrencies() {
		return currencies;
	}
}
