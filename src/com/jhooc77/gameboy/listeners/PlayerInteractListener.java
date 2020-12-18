package com.jhooc77.gameboy.listeners;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Button;
import com.jhooc77.gameboy.data.Pocket;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventCallback;
import net.minestom.server.event.handler.EventHandler;
import net.minestom.server.event.player.PlayerHandAnimationEvent;

public class PlayerInteractListener implements EventCallback<PlayerHandAnimationEvent>{

    private GameboyExtension extension;

    public PlayerInteractListener(EventHandler handler, GameboyExtension extension) {
        this.extension = extension;
        handler.addEventCallback(PlayerHandAnimationEvent.class, this);
    }
	@Override
	public void run(PlayerHandAnimationEvent event) {

        Player player = event.getPlayer();
        Pocket pocket = extension.getPlayerHandler().getPocket(player);
        if (pocket.isEmpty()) return;
        pocket.getButtonToggleHelper().press(Button.BUTTONB, true);
        event.setCancelled(true);
		
	}

}
