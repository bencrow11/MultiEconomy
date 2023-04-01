package com.bencrow11.multieconomy.storage;

// Class that stores SQL connection data
public class StorageSQL {

	private final String database;
	private final String hostname;
	private final String password;
	private final String port;
	private final String username;

	public StorageSQL(String database, String hostname, String password, String port, String username) {
		this.database = database;
		this.hostname = hostname;
		this.password = password;
		this.port = port;
		this.username = username;
	}

	public String getDatabase() {
		return database;
	}

	public String getHostname() {
		return hostname;
	}

	public String getPassword() {
		return password;
	}

	public String getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}
}
