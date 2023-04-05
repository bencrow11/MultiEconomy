package com.bencrow11.multieconomy.permission;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.UUID;


public class PermissionManager {
	public static final String BASE_PERMISSION = "multieconomy.";

	public static final String USER_PERMISSIONS = BASE_PERMISSION + "user.";
	public static final String ADMIN_PERMISSIONS = BASE_PERMISSION + "admin.";

	public static final String LOGIN_NOTIFY_PERMISSION = ADMIN_PERMISSIONS + "notify";
	public static final String ADD_BALANCE_PERMISSION = ADMIN_PERMISSIONS + "add";

	public static boolean hasPermission(UUID user, String permission) {
		User playerLP = LuckPermsProvider.get().getUserManager().getUser(user);

		if (playerLP == null) {
			System.out.println("Multieconomy could not find player " + user + " in LuckPerms.");
			return false;
		}

		return playerLP.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
	}
}
