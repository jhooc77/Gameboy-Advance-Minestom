package com.jhooc77.gameboy.handlers.map;

import java.awt.Graphics2D;
import java.util.HashMap;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Pocket;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.map.framebuffers.LargeGraphics2DFramebuffer;
import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.time.TimeUnit;
import ygba.YGBA;

public class MapHandler {
	 
	private GameboyExtension extension;
 
	private HashMap<Player, Task> mapTask = new HashMap<Player, Task>();
 
	public MapHandler(GameboyExtension plugin) {
		this.extension = plugin;
	}
 
	public void sendMap(Player player) {
 
		LargeGraphics2DFramebuffer framebuffer = new LargeGraphics2DFramebuffer(256, 256);
 
		Pocket pocket = extension.getPlayerHandler().getPocket(player);
 
		framebuffer.getRenderer().clearRect(0, 0, 256, 256);
 
		mapTask.put(player, MinecraftServer.getSchedulerManager().buildTask(() -> {
			renderer(framebuffer.getRenderer(), pocket.getEmulator());
			sendPacket(player, framebuffer, pocket.getMapId());
		}).repeat(1, TimeUnit.TICK).schedule());
 
 
	}
 
	private void sendPacket(Player player, LargeGraphics2DFramebuffer framebuffer, int mapId) {
		MapDataPacket[] packet = new MapDataPacket[4];
		packet[0] = new MapDataPacket();
		packet[1] = new MapDataPacket();
		packet[2] = new MapDataPacket();
		packet[3] = new MapDataPacket();
		packet[0].mapId = mapId;
		packet[1].mapId = mapId+1;
		packet[2].mapId = mapId+2;
		packet[3].mapId = mapId+3;
		framebuffer.createSubView(0, 0).preparePacket(packet[0]);
		framebuffer.createSubView(128, 0).preparePacket(packet[1]);
		framebuffer.createSubView(0, 128).preparePacket(packet[2]);
		framebuffer.createSubView(128, 128).preparePacket(packet[3]);
		player.getPlayerConnection().sendPacket(packet[0]);
		player.getPlayerConnection().sendPacket(packet[1]);
		player.getPlayerConnection().sendPacket(packet[2]);
		player.getPlayerConnection().sendPacket(packet[3]);
 
 
	}
 
	private void renderer(Graphics2D graphics2d, YGBA emulator) {
		graphics2d.drawImage(emulator.getGraphics().getImage(), 0, 0, null);
	}
 
	public void resetMap(Player player) {
		MinecraftServer.getSchedulerManager().removeTask(mapTask.get(player));
		mapTask.remove(player);
	}
}