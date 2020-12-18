package com.jhooc77.gameboy.handlers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Pocket;

import net.minestom.server.entity.Player;

public class PlayerHandler {

    private GameboyExtension extension;

    private Map<Player, Pocket> pockets;

    public PlayerHandler(GameboyExtension extension) {
        this.extension = extension;

        pockets =  new HashMap<>();
    }

    public void loadGame(Player player, URL cartridge, URL bios) {
        try {
            extension.notifyEmulate();
            getPocket(player).loadEmulator(extension, cartridge, player, bios);
            extension.getMapHandler().sendMap(player);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Pocket getPocket(Player player) {
        return pockets.computeIfAbsent(player, f -> new Pocket());
    }

    public void removePocket(Player player) {
        pockets.remove(player);
    }
}
