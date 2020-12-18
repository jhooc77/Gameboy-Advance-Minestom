package com.jhooc77.gameboy;

import java.io.File;

import com.jhooc77.gameboy.commands.GameboyExecutor;
import com.jhooc77.gameboy.data.Pocket;
import com.jhooc77.gameboy.handlers.PlayerHandler;
import com.jhooc77.gameboy.handlers.RomHandler;
import com.jhooc77.gameboy.handlers.map.MapHandler;
import com.jhooc77.gameboy.listeners.PlayerBlockInteractListener;
import com.jhooc77.gameboy.listeners.PlayerDropItemListener;
import com.jhooc77.gameboy.listeners.PlayerInteractListener;
import com.jhooc77.gameboy.listeners.PlayerMoveListener;
import com.jhooc77.gameboy.listeners.PlayerQuitListener;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extensions.Extension;

public class GameboyExtension extends Extension {

    private int gamesEmulated = 0;

    private MapHandler mapHandler;
    private PlayerHandler playerHandler;
    private RomHandler romHandler;

	private File dataFolder;

    @Override
    public void initialize() {
    	
        dataFolder = new File(MinecraftServer.getExtensionManager().getExtensionFolder(), this.getDescription().getName());

        mapHandler = new MapHandler(this);
        playerHandler = new PlayerHandler(this);
        romHandler = new RomHandler(this);
        

        MinecraftServer.getCommandManager().register(new GameboyExecutor(this));
        
        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
        
        new PlayerDropItemListener(handler, this);
        new PlayerBlockInteractListener(handler, this);
        new PlayerMoveListener(this);
        new PlayerQuitListener(handler, this);
        new PlayerInteractListener(handler, this);
    }

    @Override
    public void terminate() {

        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> {
            Pocket pocket = playerHandler.getPocket(player);
            if (!pocket.isEmpty()) pocket.stopEmulator(this, player);
        });
    }

    public MapHandler getMapHandler() {
        return mapHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public RomHandler getRomHandler() {
        return romHandler;
    }

    public void notifyEmulate() {
        gamesEmulated++;
    }
    
    public int getEmulated() {
    	return gamesEmulated;
    }
    
    public File getDataFolder() {
    	return dataFolder;
    }
}
