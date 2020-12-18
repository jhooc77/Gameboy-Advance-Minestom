package com.jhooc77.gameboy.handlers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.jhooc77.gameboy.GameboyExtension;

public class RomHandler {

    private GameboyExtension extension;

    private Map<String, URL> roms;
    private File romFolder;

    public RomHandler(GameboyExtension extension) {
        this.extension = extension;

        romFolder = new File(extension.getDataFolder(), "roms");
        new File(extension.getDataFolder(), "bios").mkdirs();
        if (!romFolder.exists()) romFolder.mkdirs();

        try {
            loadRoms();
        } catch (IOException exception) {
            // TODO: Make this fail and throw a nice error
        }
    }

    public void loadRoms() throws IOException {
        roms = new HashMap<>();

        for (File rom : romFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.endsWith(".gba"));
            }
        })) {
            URL cartridge = rom.toURI().toURL();
            roms.put(rom.getName(), cartridge);
        }
    }

    public Map<String, URL> getRoms() {
        return roms;
    }
}
