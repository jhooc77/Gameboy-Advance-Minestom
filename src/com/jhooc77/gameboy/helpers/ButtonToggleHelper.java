package com.jhooc77.gameboy.helpers;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jhooc77.gameboy.GameboyExtension;
import com.jhooc77.gameboy.data.Button;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.time.TimeUnit;
import ygba.YGBA;

public class ButtonToggleHelper implements Runnable {

    private GameboyExtension extension;
    private YGBA emulator;

    private Map<Button, Long> buttonLastPressTimes;
    private int buttonDebounce;
    private Task task;

    public ButtonToggleHelper(GameboyExtension extension, YGBA emulator) {
        this.extension = extension;
        this.emulator = emulator;
        buttonLastPressTimes = new HashMap<>();

        buttonDebounce = 150;

        this.task = MinecraftServer.getSchedulerManager().buildTask(this).delay(5, TimeUnit.TICK).repeat(3, TimeUnit.TICK).schedule();
    }

    public void press(Button button, Boolean state) {
        if (state) buttonLastPressTimes.put(button, System.currentTimeMillis());
        else return;

        switch (button) {
            case BUTTONA:
                emulator.getMemory().getIORegMemory().keyPressed(KeyEvent.VK_X);
                break;
            case BUTTONB:
                emulator.getMemory().getIORegMemory().keyPressed(KeyEvent.VK_C);
                break;
            case BUTTONDOWN:
                emulator.getMemory().getIORegMemory().keyPressed(KeyEvent.VK_DOWN);
                break;
            case BUTTONLEFT:
                emulator.getMemory().getIORegMemory().keyPressed(KeyEvent.VK_LEFT);
                break;
            case BUTTONRIGHT:
                emulator.getMemory().getIORegMemory().keyPressed(KeyEvent.VK_RIGHT);
                break;
            case BUTTONUP:
                emulator.getMemory().getIORegMemory().keyPressed(KeyEvent.VK_UP);
                break;
            case BUTTONSTART:
                emulator.getMemory().getIORegMemory().keyPressed(KeyEvent.VK_ENTER);
                break;
            case BUTTONSELECT:
                emulator.getMemory().getIORegMemory().keyPressed(KeyEvent.VK_SPACE);
                break;
        }
    }
    
    public void release(Button button, Boolean state) {
        if (state) buttonLastPressTimes.put(button, System.currentTimeMillis());
        else return;

        switch (button) {
            case BUTTONA:
                emulator.getMemory().getIORegMemory().keyReleased(KeyEvent.VK_X);
                break;
            case BUTTONB:
                emulator.getMemory().getIORegMemory().keyReleased(KeyEvent.VK_C);
                break;
            case BUTTONDOWN:
                emulator.getMemory().getIORegMemory().keyReleased(KeyEvent.VK_DOWN);
                break;
            case BUTTONLEFT:
                emulator.getMemory().getIORegMemory().keyReleased(KeyEvent.VK_LEFT);
                break;
            case BUTTONRIGHT:
                emulator.getMemory().getIORegMemory().keyReleased(KeyEvent.VK_RIGHT);
                break;
            case BUTTONUP:
                emulator.getMemory().getIORegMemory().keyReleased(KeyEvent.VK_UP);
                break;
            case BUTTONSTART:
                emulator.getMemory().getIORegMemory().keyReleased(KeyEvent.VK_ENTER);
                break;
            case BUTTONSELECT:
                emulator.getMemory().getIORegMemory().keyReleased(KeyEvent.VK_SPACE);
                break;
        }
    }

    @Override
    public void run() {
        Iterator<Button> buttonIterator = buttonLastPressTimes.keySet().iterator();
        while (buttonIterator.hasNext()) {
            Button button = (Button) buttonIterator.next();
            long lastPressTime = buttonLastPressTimes.get(button);
            if (System.currentTimeMillis() - lastPressTime > buttonDebounce) {
                release(button, true);
                buttonIterator.remove();
            }
        }
    }
    
    public Task getTask() {
    	return this.task;
    }
}
