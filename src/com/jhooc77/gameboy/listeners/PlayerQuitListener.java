package com.jhooc77.gameboy.listeners;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Pocket;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventCallback;
import net.minestom.server.event.handler.EventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class PlayerQuitListener implements EventCallback<PlayerDisconnectEvent> {

    private GameboyExtension extension;

    public PlayerQuitListener(EventHandler handler, GameboyExtension extension) {
        this.extension = extension;
        handler.addEventCallback(PlayerDisconnectEvent.class, this);
    }

	@Override
	public void run(PlayerDisconnectEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = extension.getPlayerHandler().getPocket(player);
        if (!pocket.isEmpty()) pocket.stopEmulator(extension, player);
		
	}
}
