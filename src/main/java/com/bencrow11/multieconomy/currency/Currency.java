package com.bencrow11.multieconomy.currency;

/**
 * Immutable class the store a represent a single currency.
 */
public class Currency {

	private final String name; // The currency name.
	private final String singular; // The currencies singular name (dollar).
	private final String plural; // The currencies plural name (dollars).
	private final float startBalance; // The currencies starting balance.
	private final boolean allowPayments; // Whether players can use /pay with this currency.

	/**
	 * Constructor to create a new currency.
	 * @param name assigned to the name field.
	 * @param singular assigned to the singular field.
	 * @param plural assigned to the plural field.
	 * @param startBalance assigned to the startBalance field.
	 * @param allowPayments assigned to the allowPayments field.
	 */
	public Currency(String name, String singular, String plural, int startBalance, boolean allowPayments) {
		this.name = name;
		this.singular = singular;
		this.plural = plural;
		this.startBalance = startBalance;
		this.allowPayments = allowPayments;
	}

	/**
	 * Getter for the name field
	 * @return string that represents the currencies name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for the singular field.
	 * @return string that represents the currencies singular name value.
	 */
	public String getSingular() {
		return singular;
	}

	/**
	 * Getter for the plural field.
	 * @return string that represents the currencies plural name value.
	 */
	public String getPlural() {
		return plural;
	}

	/**
	 * Getter for the startBalance field.
	 * @return float that represents the currencies starting balance.
	 */
	public float getStartBalance() {
		return startBalance;
	}

	/**
	 * Getter for the allowPayments field.
	 * @return boolean that represents if the currency allows payments.
	 */
	public boolean isAllowPayments() {
		return allowPayments;
	}
}
