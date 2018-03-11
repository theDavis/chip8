package io.github.thedavis.chip8;

import java.util.Map;
import java.util.Optional;

public class Chip8 {

    private final boolean debug;

    private final CPU cpu;
    private final Graphics graphics;
    private final Sound sound;
    private final InputDevice inputDevice;

    public Chip8(boolean debug){
        this.debug = debug;
        this.cpu = new CPU();
        this.graphics = new Graphics();
        this.sound = new Sound();
        this.inputDevice = new InputDevice();
    }

    public void initialize(){
        //TODO: implement intializing components
    }

    public void loadRom(){
        //TODO: implement loading rom
    }

    public void run(){
        //TODO: implement main loop
    }

    public static void main(String[] args){
        Chip8 chip8 = new Chip8(false);
        chip8.initialize();
        chip8.loadRom();
        chip8.run();
    }
}
