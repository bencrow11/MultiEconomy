package com.bencrow11.multieconomy.currency;

// Class that stores a single currency.
public class Currency {

	private final String name;
	private final String singular;
	private final String plural;
	private final float startBalance;
	private final boolean allowPayments;

	public Currency(String name, String singular, String plural, int startBalance, boolean allowPayments) {
		this.name = name;
		this.singular = singular;
		this.plural = plural;
		this.startBalance = startBalance;
		this.allowPayments = allowPayments;
	}

	public String getName() {
		return name;
	}

	public String getSingular() {
		return singular;
	}

	public String getPlural() {
		return plural;
	}

	public float getStartBalance() {
		return startBalance;
	}

	public boolean isAllowPayments() {
		return allowPayments;
	}
}
