package com.jhooc77.gameboy.listeners;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Button;
import com.jhooc77.gameboy.data.Pocket;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventCallback;
import net.minestom.server.event.handler.EventHandler;
import net.minestom.server.event.item.ItemDropEvent;

public class PlayerDropItemListener implements EventCallback<ItemDropEvent> {

    private GameboyExtension extension;

    public PlayerDropItemListener(EventHandler handler, GameboyExtension extension) {
        this.extension = extension;
        handler.addEventCallback(ItemDropEvent.class, this);
    }

	@Override
	public void run(ItemDropEvent event) {
        Player player = event.getPlayer();
        Pocket pocket = extension.getPlayerHandler().getPocket(player);
        if (!pocket.isEmpty())  {
            pocket.getButtonToggleHelper().press(Button.BUTTONSELECT, true);
            event.setCancelled(true);
        }
	}
}
