/**
 * This file is part of MultiEconomy.
 *
 * MultiEconomy is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * MultiEconomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package com.bencrow11.multieconomy.permission;

import com.bencrow11.multieconomy.Multieconomy;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.UUID;

/**
 * Class that deals with all MultiEconomy permissions.
 */
public abstract class PermissionManager {
	public static final String BASE_PERMISSION = "multieconomy."; // Base permission for all permissions

	public static final String USER_PERMISSIONS = BASE_PERMISSION + "user."; // Permission for users
	public static final String ADMIN_PERMISSIONS = BASE_PERMISSION + "admin."; // Permission for admins

	public static final String PAY_PERMISSION = USER_PERMISSIONS + "command.pay"; // Permission to /pay
	public static final String BAL_PERMISSION = USER_PERMISSIONS + "command.bal"; // Permission to /bal
	public static final String BALTOP_PERMISSION = USER_PERMISSIONS + "command.baltop"; // Permission to /baltop

	public static final String HELP_EXTRAS_PERMISSION = ADMIN_PERMISSIONS + "help"; // Permission to admin help
	public static final String LOGIN_NOTIFY_PERMISSION = ADMIN_PERMISSIONS + "notify"; // Permission to error notifications.
	public static final String ADD_BALANCE_PERMISSION = ADMIN_PERMISSIONS + "add"; // Permission to /meco add
	public static final String REMOVE_BALANCE_PERMISSION = ADMIN_PERMISSIONS + "remove"; // Permission to /meco remove
	public static final String SET_BALANCE_PERMISSION = ADMIN_PERMISSIONS + "set"; // Permission to /meco set
	public static final String CLEAR_BALANCE_PERMISSION = ADMIN_PERMISSIONS + "clear"; // Permission to /meco clear
	public static final String RELOAD_PERMISSION = ADMIN_PERMISSIONS + "reload"; // Permission to /meco reload

	/**
	 * Checks a user has a given permission.
	 * @param user The user to check the permission on.
	 * @param permission The permission to check the user has.
	 * @return true if the user has the permission.
	 */
	public static boolean hasPermission(UUID user, String permission) {
		User playerLP = LuckPermsProvider.get().getUserManager().getUser(user);

		if (playerLP == null) {
			Multieconomy.LOGGER.error("Multieconomy could not find player " + user + " in LuckPerms.");
			return false;
		}

		return playerLP.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
	}
}
