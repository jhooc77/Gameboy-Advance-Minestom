package com.jhooc77.gameboy.listeners;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Button;
import com.jhooc77.gameboy.data.Pocket;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.listener.manager.PacketController;
import net.minestom.server.network.packet.client.ClientPlayPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;

public class PlayerMoveListener {

    private GameboyExtension extension;

    public PlayerMoveListener(GameboyExtension extension) {
        this.extension = extension;
        MinecraftServer.getConnectionManager().onPacketReceive(this::MoveListener);
    }

	private void MoveListener(Player player, PacketController controller, ClientPlayPacket packet) {
        if (packet instanceof ClientPlayerPositionPacket) {
        	ClientPlayerPositionPacket pk = (ClientPlayerPositionPacket) packet;
            Pocket pocket = extension.getPlayerHandler().getPocket(player);
            if (pocket.isEmpty()) return;

            double diffX = pk.x - player.getPosition().getX();
            double diffZ = pk.z - player.getPosition().getZ();
            double diffY = pk.y - player.getPosition().getY();
            pocket.getButtonToggleHelper().press(Button.BUTTONUP, diffX > 0.1);
            pocket.getButtonToggleHelper().press(Button.BUTTONDOWN, diffX < -0.1);
            pocket.getButtonToggleHelper().press(Button.BUTTONRIGHT, diffZ > 0.1);
            pocket.getButtonToggleHelper().press(Button.BUTTONLEFT, diffZ < -0.1);
            pocket.getButtonToggleHelper().press(Button.BUTTONA, diffY > 0.1);
            controller.setCancel(true);
            player.teleport(player.getPosition());
        }else if (packet instanceof ClientPlayerPositionAndRotationPacket) {
        	ClientPlayerPositionAndRotationPacket pk = (ClientPlayerPositionAndRotationPacket) packet;
            Pocket pocket = extension.getPlayerHandler().getPocket(player);
            if (pocket.isEmpty()) return;

            double diffX = pk.x - player.getPosition().getX();
            double diffZ = pk.z - player.getPosition().getZ();
            double diffY = pk.y - player.getPosition().getY();
            pocket.getButtonToggleHelper().press(Button.BUTTONUP, diffX > 0.1);
            pocket.getButtonToggleHelper().press(Button.BUTTONDOWN, diffX < -0.1);
            pocket.getButtonToggleHelper().press(Button.BUTTONRIGHT, diffZ > 0.1);
            pocket.getButtonToggleHelper().press(Button.BUTTONLEFT, diffZ < -0.1);
            pocket.getButtonToggleHelper().press(Button.BUTTONA, diffY > 0.1);
            controller.setCancel(true);
        }
	}

}
