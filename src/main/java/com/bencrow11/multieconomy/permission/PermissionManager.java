package com.bencrow11.multieconomy.permission;

import com.bencrow11.multieconomy.Multieconomy;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.UUID;


public class PermissionManager {
	public static final String BASE_PERMISSION = "multieconomy.";

	public static final String USER_PERMISSIONS = BASE_PERMISSION + "user.";
	public static final String ADMIN_PERMISSIONS = BASE_PERMISSION + "admin.";

	public static final String PAY_PERMISSION = USER_PERMISSIONS + "command.pay";
	public static final String BAL_PERMISSION = USER_PERMISSIONS + "command.bal";
	public static final String BALTOP_PERMISSION = USER_PERMISSIONS + "command.baltop";

	public static final String HELP_EXTRAS_PERMISSION = ADMIN_PERMISSIONS + "help";
	public static final String LOGIN_NOTIFY_PERMISSION = ADMIN_PERMISSIONS + "notify";
	public static final String ADD_BALANCE_PERMISSION = ADMIN_PERMISSIONS + "add";
	public static final String REMOVE_BALANCE_PERMISSION = ADMIN_PERMISSIONS + "remove";
	public static final String SET_BALANCE_PERMISSION = ADMIN_PERMISSIONS + "set";
	public static final String CLEAR_BALANCE_PERMISSION = ADMIN_PERMISSIONS + "clear";
	public static final String RELOAD_PERMISSION = ADMIN_PERMISSIONS + "reload";

	public static boolean hasPermission(UUID user, String permission) {
		User playerLP = LuckPermsProvider.get().getUserManager().getUser(user);

		if (playerLP == null) {
			Multieconomy.LOGGER.error("Multieconomy could not find player " + user + " in LuckPerms.");
			return false;
		}

		return playerLP.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
	}
}
