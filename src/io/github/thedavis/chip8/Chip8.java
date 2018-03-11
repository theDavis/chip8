package io.github.thedavis.chip8;

public class Chip8 {

    private final CPU cpu;
    private final Graphics graphics;
    private final Sound sound;
    private final InputDevice inputDevice;

    public Chip8(){
        this.cpu = new CPU();
        this.graphics = new Graphics();
        this.sound = new Sound();
        this.inputDevice = new InputDevice();
    }

    public void initialize(){
        //TODO: implement
    }

    public void loadRom(){
        //TODO: implement
    }

    public static void main(String[] args){

        Chip8 chip8 = new Chip8();
        chip8.initialize();

        chip8.loadRom();
    }

    public static Map<String,String> parseArgs(String[] args){

    }
}
