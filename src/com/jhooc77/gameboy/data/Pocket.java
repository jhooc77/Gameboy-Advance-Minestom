package com.jhooc77.gameboy.data;

import java.io.File;
import java.net.URL;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.helpers.ButtonToggleHelper;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.type.decoration.EntityItemFrame;
import net.minestom.server.entity.type.decoration.EntityItemFrame.ItemFrameOrientation;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.MapMeta;
import net.minestom.server.utils.BlockPosition;
import net.minestom.server.utils.Position;
import ygba.YGBA;

public class Pocket {

    private YGBA emulator;

    private ButtonToggleHelper buttonToggleHelper;

    public EntityItemFrame frameEntity[] = null;
    
    private int mapId;

    public void loadEmulator(GameboyExtension extension, URL cartridge, Player player, URL bios) {
        emulator = new YGBA();
        
        emulator.stop();
        
        emulator.getMemory().unloadBIOS();
        emulator.getMemory().unloadROM();
        
        emulator.getMemory().loadBIOS(bios);
        
        emulator.getMemory().loadROM(cartridge);

        buttonToggleHelper = new ButtonToggleHelper(extension, emulator);
        
        
        BlockPosition block = player.getPosition().clone().add(0, 1, 0).toBlockPosition();

        Position pos = player.getPosition().clone().toBlockPosition().toPosition().add(0, 0, 1);
        pos.setPitch(0);
        pos.setYaw(-90);
		player.teleport(pos);
		
		mapId = extension.getEmulated()*4;
		
        frameEntity = new EntityItemFrame[] {
        		new EntityItemFrame(block.clone().add(1, 1, 0).toPosition(), ItemFrameOrientation.WEST) {
        			{
        				setView(90, 0);
        				setInstance(player.getInstance());
        				setNoGravity(true);
        				setItemStack(new ItemStack(Material.FILLED_MAP, (byte) 1) {
        					{
        						MapMeta meta = (MapMeta) getItemMeta();
        						meta.setMapId(mapId);
        						setItemMeta(meta);
        					}
        				});
        			}
        		},
        		new EntityItemFrame(block.clone().add(1, 1, 1).toPosition(), ItemFrameOrientation.WEST) {
        			{
        				setView(90, 0);
        				setInstance(player.getInstance());
        				setNoGravity(true);
        				setItemStack(new ItemStack(Material.FILLED_MAP, (byte) 1) {
        					{
        						MapMeta meta = (MapMeta) getItemMeta();
        						meta.setMapId(mapId+1);
        						setItemMeta(meta);
        					}
        				});
        			}
        		},
        		new EntityItemFrame(block.clone().add(1, 0, 0).toPosition(), ItemFrameOrientation.WEST) {
        			{
        				setView(90, 0);
        				setInstance(player.getInstance());
        				setNoGravity(true);
        				setItemStack(new ItemStack(Material.FILLED_MAP, (byte) 1) {
        					{
        						MapMeta meta = (MapMeta) getItemMeta();
        						meta.setMapId(mapId+2);
        						setItemMeta(meta);
        					}
        				});
        			}
        		},
        		new EntityItemFrame(block.clone().add(1, 0, 1).toPosition(), ItemFrameOrientation.WEST) {
        			{
        				setView(90, 0);
        				setInstance(player.getInstance());
        				setNoGravity(true);
        				setItemStack(new ItemStack(Material.FILLED_MAP, (byte) 1) {
        					{
        						MapMeta meta = (MapMeta) getItemMeta();
        						meta.setMapId(mapId+3);
        						setItemMeta(meta);
        					}
        				});
        			}
        		}
        };

        emulator.reset();
        
        if (emulator.isReady()) {
        	emulator.run();
        }

    }

    public void stopEmulator(GameboyExtension extension, Player player) {

    	extension.getMapHandler().resetMap(player);
        
    	frameEntity[0].remove();
    	frameEntity[1].remove();
    	frameEntity[2].remove();
    	frameEntity[3].remove();
    	
        emulator.stop();

        MinecraftServer.getSchedulerManager().removeTask(buttonToggleHelper.getTask());
        
        emulator = null;
    }

    public void createSavesFolder(GameboyExtension plugin, Player player) {
        File savesFolder = new File(plugin.getDataFolder(), "saves/" + player.getUuid().toString());
        savesFolder.mkdirs();
    }

    public boolean isEmpty() {
        return emulator == null;
    }

    public YGBA getEmulator() {
        return emulator;
    }

    public ButtonToggleHelper getButtonToggleHelper() {
        return buttonToggleHelper;
    }
    
    public int getMapId() {
    	return this.mapId;
    }
}
