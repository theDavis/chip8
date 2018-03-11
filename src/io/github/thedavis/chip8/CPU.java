package io.github.thedavis.chip8;

import java.io.File;

public class CPU {
    private short[] registers = new short[16];
    private int indexRegister = 0;

    private int programCounter = 200;
    private int opCode = 0;

    private short[] stack = new short[16];
    private short stackPointer = 0;

    private short[] memory = new short[4096];

    private short delayTimer = 0;
    private short soundTimer = 0;

    public void initialize(){
        loadFonts();
    }

    private void loadFonts(){
        //TODO: load fonts into memory locations [0-80]
    }

    public void loadRom(File rom){
        //TODO: Load rom starting at memory location 200
    }

    public void step(){
        fetch();
        decode();
        execute();
        updateTimers();
    }

    private void fetch(){
        //TODO: fetch next opcode from memory using the PC
    }

    private void decode(){
        //TODO: decode the opcode and perform
    }

    private void execute(){
        //TODO: execute the operation specified by opcode
    }
    private void updateTimers(){
        //TODO: handle timers and figure out how to keep 60hz
    }

}
