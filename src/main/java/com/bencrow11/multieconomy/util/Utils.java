package com.bencrow11.multieconomy.util;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.Multieconomy;
import com.bencrow11.multieconomy.config.Config;
import com.bencrow11.multieconomy.currency.Currency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Consumer;

public class Utils {

	public static final String BASE_PATH = new File("").getAbsolutePath() + "/config/MultiEconomy/";

	/**
	 * Method to write some data to file.
	 * @param filePath the directory to write the file to
	 * @param filename the name of the file
	 * @param data the data to write to file
	 * @return true if writing to file was successful
	 */
	public static boolean writeFileAsync(String filePath, String filename, String data) {
		try {
			Path path = Paths.get(BASE_PATH + filePath + filename);

			if (!Files.exists(Paths.get(BASE_PATH + filePath))) {
				Files.createDirectory(Path.of(BASE_PATH + filePath));
			}

			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			buffer.put(data.getBytes());
			buffer.flip();

			fileChannel.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					attachment.clear();
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					exc.printStackTrace();
				}
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method to read a file asynchronously
	 * @param filePath the path of the directory to find the file at
	 * @param filename the name of the file
	 * @param callback a callback to deal with the data read
	 * @return true if the file was read successfully
	 */
	public static boolean readFileAsync(String filePath, String filename, Consumer<String> callback) {
		try {
			Path path = Paths.get(BASE_PATH + filePath + filename);

			if (!Files.exists(Paths.get(BASE_PATH + filePath))) {
				return false;
			}

			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
			ByteBuffer buffer = ByteBuffer.allocate(1024);

			fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					attachment.flip();
					byte[] data = new byte[attachment.limit()];
					attachment.get(data);
					callback.accept(new String(data));
					attachment.clear();
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					exc.printStackTrace();
				}
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}

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

	public static File checkForDirectory(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	private static String[] findFileName(File dir, String filename) {
		return dir.list((dir1, name) -> name.equals(filename));
	}

	public static Gson newGson() {
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

	public static String formatMessage(String message, Boolean isPlayer) {
		if (isPlayer) {
			return message.trim();
		} else {
			return message.replaceAll("§[0-9a-fk-or]", "").trim();
		}
	}
}
