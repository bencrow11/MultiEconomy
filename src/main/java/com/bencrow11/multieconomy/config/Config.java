package com.bencrow11.multieconomy.config;

import com.bencrow11.multieconomy.currency.Currency;

import java.util.ArrayList;

public class Config {
	private final boolean allowOfflinePayments;
	private final String defaultCurrency;
	private final ArrayList<Currency> currencies;

	public Config(boolean allowOfflinePayments, String defaultCurrency,
	              ArrayList<Currency> currencies) {
		this.allowOfflinePayments = allowOfflinePayments;
		this.defaultCurrency = defaultCurrency;
		this.currencies = currencies;
	}

	public boolean isAllowOfflinePayments() {
		return allowOfflinePayments;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public ArrayList<Currency> getCurrencies() {
		return currencies;
	}
}
