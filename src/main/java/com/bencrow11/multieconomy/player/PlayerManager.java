package com.bencrow11.multieconomy.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public abstract class PlayerManager {
	private static HashMap<UUID, Player> players = new HashMap<>();
	private static final String PATH = new File("").getAbsolutePath() + "/config/MultiEconomy/playerdata";

	public static void addPlayer(Player player) {
		players.put(player.getUuid(), player);
		writeToFile(player);
	}

	public static boolean hasPlayer(UUID uuid) {
		return players.containsKey(uuid);
	}

	public static Player getPlayerByName(String name) {
		for (Player player : players.values()) {
			if (player.getName().toLowerCase().trim().equals(name.toLowerCase().trim())) {
				return player;
			}
		}
		return null;
	}

	public static Player getPlayerByUUID(UUID uuid) {
		return players.get(uuid);
	}

	public static void updatePlayer(Player player) {
		players.remove(player.getUuid());
		addPlayer(player);
	}

	private static boolean writeToFile(Player player) {
		try {
			File dir = new File(PATH);

			String[] list = dir.list((dir1, name) -> name.equals(player.getUuid().toString() + ".json"));

			File file = new File(dir, player.getUuid().toString() + ".json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			if (list.length == 0) {
				file.createNewFile();
			}

			Writer writer = new FileWriter(file, false);

			gson.toJson(player, writer);
			writer.flush();
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Reads a players data from file and loads it into players hashmap.
	 * @return true if successfully loaded from file.
	 */
	public static boolean loadFromFile() {

		try {
			File dir = new File(PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String[] list = dir.list();

			if (list.length == 0) {
				return true;
			}

			for (String item : list) {
				File file = new File(dir, item);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();

				Reader reader = new FileReader(file);

				Player player = gson.fromJson(reader, Player.class);

				reader.close();
				players.put(player.getUuid(), player);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
