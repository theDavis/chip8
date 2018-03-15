package io.github.thedavis.chip8;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CPU {
    private static final int ROM_START = 0x200;

    private short[] registers = new short[16];
    private int indexRegister = 0;

    private int programCounter = ROM_START;
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

    public void loadRom(File rom) throws IOException {
        try(FileInputStream fis = new FileInputStream(rom)){
            int nextByte = 0;
            int location = ROM_START;
            while((nextByte = fis.read()) >= 0){
                write(location++, nextByte);
            }
        }
    }

    public void dumpMemory(){
        for(int i = 0; i < memory.length; i = i+2){
            int current = read(i);
            current = current << 8;
            current += read(i+1);
            System.out.println(Integer.toHexString(current));
        }
    }

    public void write(int location, int value){
        memory[location] = (short) (value & 0xFF);
    }

    public short read(int location){
        return (short) (memory[location] & 0xFF);
    }

    public void step(){
        execute(decode(fetch()));
        updateTimers();
    }

    private int fetch(){
        int instruction = (read(programCounter) << 8) + read(programCounter+1);
        programCounter += 2;
        return instruction;
    }

    private int[] decode(int instruction){
        int operand = (instruction & 0x0FFFF);
        int opCode = (operand & 0x0F000) >> 12;
        return new int[]{opCode,operand};
    }

    private void execute(int[] decodedInstruction){
        switch(decodedInstruction[0]){
            case 0x0:
                System.out.println("Bunch of options");
                break;
            case 0x1:
                System.out.println("Jump to "+Integer.toHexString(decodedInstruction[1]));
                break;
            case 0x2:
                System.out.println("Call sub "+Integer.toHexString(decodedInstruction[1]));
                break;
            case 0x3:
                System.out.println("Skip next if Vx == NN");
                break;
            case 0x4:
                System.out.println("Skip next if Vx != NN");
                break;
            case 0x5:
                System.out.println("Skip next if Vx == Vy");
                break;
            case 0x6:
                int register = (decodedInstruction[1] & 0x0F00) >> 8;
                int value = decodedInstruction[1] & 0xFF;
                registers[register] = (short) value;
                System.out.println("Set V"+Integer.toHexString(register)+" to "+Integer.toHexString(value));
                break;
            case 0x7:
                System.out.println("Add NN to Vx (no carry)");
                break;
            case 0x8:
                System.out.println("Bunch of options");
                break;
            case 0x9:
                System.out.println("Skip next if Vx != Vy");
                break;
            case 0xA:
                int address = decodedInstruction[1] & 0x0FFF;
                indexRegister = address;
                System.out.println("Set I to "+Integer.toHexString(address));
                break;
            case 0xB:
                System.out.println("Jump to V0 + "+Integer.toHexString(decodedInstruction[1]));
                break;
            case 0xC:
                System.out.println("Random");
                break;
            case 0xD:
                System.out.println("Draw");
                break;
            case 0xE:
                System.out.println("Bunch of things");
                break;
            case 0xF:
                System.out.println("Bunch of things");
                break;
            default:
                throw new IllegalArgumentException("Invalid opcode: "+Integer.toHexString(decodedInstruction[0]));
        }
    }
    private void updateTimers(){
        //TODO: handle timers and figure out how to keep 60hz
    }

}
