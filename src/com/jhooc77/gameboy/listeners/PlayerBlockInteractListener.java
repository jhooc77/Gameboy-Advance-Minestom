package com.jhooc77.gameboy.listeners;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Pocket;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventCallback;
import net.minestom.server.event.handler.EventHandler;
import net.minestom.server.event.player.PlayerBlockInteractEvent;

public class PlayerBlockInteractListener implements EventCallback<PlayerBlockInteractEvent>{

    private GameboyExtension extension;

    public PlayerBlockInteractListener(EventHandler handler, GameboyExtension extension) {
        this.extension = extension;
        handler.addEventCallback(PlayerBlockInteractEvent.class, this);
    }

	@Override
	public void run(PlayerBlockInteractEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = extension.getPlayerHandler().getPocket(player);
        if (pocket.isEmpty()) return;
        event.setCancelled(true);
		
	}
}
