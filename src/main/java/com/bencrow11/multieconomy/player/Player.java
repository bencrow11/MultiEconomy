package com.bencrow11.multieconomy.player;

import java.util.UUID;

public class Player {
	private String name;
	private UUID uuid;

	public Player(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}
}
