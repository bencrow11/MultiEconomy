package com.bencrow11.multieconomy.forge.events;

import com.bencrow11.multieconomy.ErrorManager;
import com.bencrow11.multieconomy.events.PlayerJoinHandler;
import com.bencrow11.multieconomy.permission.PermissionManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerJoinEvent {

	@SubscribeEvent
	public void playerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
		new PlayerJoinHandler(event.getEntity().getName().getString(), event.getEntity().getUUID());

		// If the player has the "notify" perm, notify of any errors.
		if (PermissionManager.hasPermission(event.getEntity().getUUID(),
				PermissionManager.LOGIN_NOTIFY_PERMISSION)) {
			ErrorManager.printErrorsToPlayer(event.getEntity());
		}
	}

}
