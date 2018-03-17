package io.github.thedavis.chip8;

import io.github.thedavis.chip8.cpu.CPU;
import io.github.thedavis.chip8.cpu.RegisterBlock;
import io.github.thedavis.chip8.memory.Memory;

public class Chip8 {

    boolean debugParam;

    String romParam;

    private final boolean debug;
    private final CPU cpu;
    private final Graphics graphics;
    private final Sound sound;
    private final InputDevice inputDevice;

    public Chip8(boolean debug){
        this.debug = debug;
        this.cpu = new CPU(new Memory(), new RegisterBlock());
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
