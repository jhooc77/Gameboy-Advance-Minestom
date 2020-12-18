package com.jhooc77.gameboy.handlers.map;

import java.util.HashMap;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Pocket;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.map.framebuffers.Graphics2DFramebuffer;
import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.time.TimeUnit;
import ygba.YGBA;

public class MapHandler {

	private GameboyExtension extension;
	
	private HashMap<Player, Task> mapTask = new HashMap<Player, Task>();
	
	public MapHandler(GameboyExtension extension) {
		this.extension = extension;
	}

	public void sendMap(Player player) {
		
		Graphics2DFramebuffer[] framebuffer = {
				new Graphics2DFramebuffer(),
				new Graphics2DFramebuffer(),
				new Graphics2DFramebuffer(),
				new Graphics2DFramebuffer()
		};
		
		Pocket pocket = extension.getPlayerHandler().getPocket(player);
		
		framebuffer[0].getRenderer().clearRect(0, 0, 256, 256);
		framebuffer[1].getRenderer().clearRect(0, 0, 256, 256);
		framebuffer[2].getRenderer().clearRect(0, 0, 256, 256);
		framebuffer[3].getRenderer().clearRect(0, 0, 256, 256);
		
		mapTask.put(player, MinecraftServer.getSchedulerManager().buildTask(() -> {
			renderer(framebuffer, pocket.getEmulator());
			sendPacket(player, framebuffer, pocket.getMapId());
		}).repeat(1, TimeUnit.TICK).schedule());

		
	}
	
	private void sendPacket(Player player, Graphics2DFramebuffer[] framebuffer, int mapId) {
		MapDataPacket[] packet = new MapDataPacket[4];
		packet[0] = new MapDataPacket();
		packet[1] = new MapDataPacket();
		packet[2] = new MapDataPacket();
		packet[3] = new MapDataPacket();
		packet[0].mapId = mapId;
		packet[1].mapId = mapId+1;
		packet[2].mapId = mapId+2;
		packet[3].mapId = mapId+3;
		framebuffer[0].preparePacket(packet[0]);
		framebuffer[1].preparePacket(packet[1]);
		framebuffer[2].preparePacket(packet[2]);
		framebuffer[3].preparePacket(packet[3]);
		player.getPlayerConnection().sendPacket(packet[0]);
		player.getPlayerConnection().sendPacket(packet[1]);
		player.getPlayerConnection().sendPacket(packet[2]);
		player.getPlayerConnection().sendPacket(packet[3]);
		
		
	}

	private void renderer(Graphics2DFramebuffer[] framebuffer, YGBA emulator) {
		framebuffer[0].getRenderer().drawImage(emulator.getGraphics().getImage(), 0, 0, null);
		framebuffer[1].getRenderer().drawImage(emulator.getGraphics().getImage(), 0, 0, 112, 128, 128, 0, 240, 128, null);
		framebuffer[2].getRenderer().drawImage(emulator.getGraphics().getImage(), 0, 0, 128, 32, 0, 128, 128, 160, null);
		framebuffer[3].getRenderer().drawImage(emulator.getGraphics().getImage(), 0, 0, 112, 32, 128, 128, 240, 160, null);
	}
	
	public void resetMap(Player player) {
		MinecraftServer.getSchedulerManager().removeTask(mapTask.get(player));
		mapTask.remove(player);
	}
}
