package com.jhooc77.gameboy.commands;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Pocket;

import net.minestom.server.chat.ColoredText;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class GameboyExecutor extends Command {

    private GameboyExtension extension;

    public GameboyExecutor(GameboyExtension extension) {
    	super("gba");
        this.extension = extension;
        setCondition(this::condition);
        
        setDefaultExecutor(this::help);
        
        Argument<String> mode1 = ArgumentType.Word("mode").from("stop", "reload");
        setArgumentCallback(this::error, mode1);
        
        Argument<String> mode2 = ArgumentType.Word("mode").from("start");
        setArgumentCallback(this::error, mode2);
        
        Argument<String> game = ArgumentType.String("game");
        setArgumentCallback(this::error, game);
        
        addSyntax(this::arg1, mode1);
        
        addSyntax(this::playgame, mode2, game);
    }
    
    private void arg1(CommandSender sender, Arguments args) {

        Player player = (Player) sender;
        Pocket pocket = extension.getPlayerHandler().getPocket(player);
        switch(args.getWord("mode")) {
        case("stop"):
            if (pocket.isEmpty()) {
                player.sendMessage(colorTranslate("&c���� ���� ������ �����ϴ�"));
                return;
            }
            pocket.stopEmulator(extension, player);
            player.sendMessage(colorTranslate("&a������ �����մϴ�"));
        	break;
        case("reload"):
            try {
            	extension.getRomHandler().loadRoms();
                sender.sendMessage(colorTranslate("&a���ε� �Ϸ�"));
            } catch (IOException exception) {
                sender.sendMessage(colorTranslate("&c���ε� ����"));
            }
        	break;
        }
    }
    
    private void playgame(CommandSender sender, Arguments args) {

        Player player = (Player) sender;
        Pocket pocket = extension.getPlayerHandler().getPocket(player);
        
        if (!pocket.isEmpty()) {
            player.sendMessage(colorTranslate("&c�̹� ������ �������Դϴ�"));
            return;
        }
        String gameName = args.getString("game");
        URL foundCartridge = extension.getRomHandler().getRoms().get(gameName);
        if (foundCartridge == null) {
            sender.sendMessage(colorTranslate("&c������ ã�� �� �����ϴ�"));
            return;
        }
        URL bios = null;
		try {
			bios = new File(extension.getDataFolder(), "bios/gba_bios.bin").toURI().toURL();
		} catch (Exception e) {
			sender.sendMessage(colorTranslate("&c���̿����� ���� �� �����ϴ�"));
			return;
		}
        player.sendMessage(colorTranslate("&a���� ����: " + gameName));
		extension.getPlayerHandler().loadGame(player, foundCartridge, bios);
        return;
    }

    private void error(CommandSender sender, String value, int error) {
        sender.sendMessage(colorTranslate("&7�÷��� ���� ����: " + String.join("&r&e, &7", extension.getRomHandler().getRoms().keySet())));
        sender.sendMessage(colorTranslate("&7"));
        sender.sendMessage(colorTranslate("&7/gba start [name] : ���� ����"));
        sender.sendMessage(colorTranslate("&7/gba stop : ���� ����"));
        sender.sendMessage(colorTranslate("&7/gba reload"));
    }
    
    private boolean condition(CommandSender sender, String commandString) {
        if (!sender.isPlayer()) {
            return false;
        }
        return true;
    }
    
    private void help(CommandSender sender, Arguments args) {
        sender.sendMessage(colorTranslate("&7�÷��� ���� ����: " + String.join("&r&e, &7", extension.getRomHandler().getRoms().keySet())));
        sender.sendMessage(colorTranslate("&7"));
        sender.sendMessage(colorTranslate("&7/gba start [name] : ���� ����"));
        sender.sendMessage(colorTranslate("&7/gba stop : ���� ����"));
        sender.sendMessage(colorTranslate("&7/gba reload"));
    }

    public ColoredText colorTranslate(String message) {
        return ColoredText.ofLegacy(message, '&');
    }
}
