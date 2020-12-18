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
                player.sendMessage(colorTranslate("&c진행 중인 게임이 없습니다"));
                return;
            }
            pocket.stopEmulator(extension, player);
            player.sendMessage(colorTranslate("&a게임을 종료합니다"));
        	break;
        case("reload"):
            try {
            	extension.getRomHandler().loadRoms();
                sender.sendMessage(colorTranslate("&a리로드 완료"));
            } catch (IOException exception) {
                sender.sendMessage(colorTranslate("&c리로드 실패"));
            }
        	break;
        }
    }
    
    private void playgame(CommandSender sender, Arguments args) {

        Player player = (Player) sender;
        Pocket pocket = extension.getPlayerHandler().getPocket(player);
        
        if (!pocket.isEmpty()) {
            player.sendMessage(colorTranslate("&c이미 게임이 진행중입니다"));
            return;
        }
        String gameName = args.getString("game");
        URL foundCartridge = extension.getRomHandler().getRoms().get(gameName);
        if (foundCartridge == null) {
            sender.sendMessage(colorTranslate("&c게임을 찾을 수 없습니다"));
            return;
        }
        URL bios = null;
		try {
			bios = new File(extension.getDataFolder(), "bios/gba_bios.bin").toURI().toURL();
		} catch (Exception e) {
			sender.sendMessage(colorTranslate("&c바이오스를 읽을 수 없습니다"));
			return;
		}
        player.sendMessage(colorTranslate("&a게임 시작: " + gameName));
		extension.getPlayerHandler().loadGame(player, foundCartridge, bios);
        return;
    }

    private void error(CommandSender sender, String value, int error) {
        sender.sendMessage(colorTranslate("&7플레이 가능 게임: " + String.join("&r&e, &7", extension.getRomHandler().getRoms().keySet())));
        sender.sendMessage(colorTranslate("&7"));
        sender.sendMessage(colorTranslate("&7/gba start [name] : 게임 실행"));
        sender.sendMessage(colorTranslate("&7/gba stop : 게임 종료"));
        sender.sendMessage(colorTranslate("&7/gba reload"));
    }
    
    private boolean condition(CommandSender sender, String commandString) {
        if (!sender.isPlayer()) {
            return false;
        }
        return true;
    }
    
    private void help(CommandSender sender, Arguments args) {
        sender.sendMessage(colorTranslate("&7플레이 가능 게임: " + String.join("&r&e, &7", extension.getRomHandler().getRoms().keySet())));
        sender.sendMessage(colorTranslate("&7"));
        sender.sendMessage(colorTranslate("&7/gba start [name] : 게임 실행"));
        sender.sendMessage(colorTranslate("&7/gba stop : 게임 종료"));
        sender.sendMessage(colorTranslate("&7/gba reload"));
    }

    public ColoredText colorTranslate(String message) {
        return ColoredText.ofLegacy(message, '&');
    }
}
