package io.github.thedavis.chip8.cpu;

import io.github.thedavis.chip8.memory.Memory;
import io.github.thedavis.chip8.memory.MemoryOutOfBoundsException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CPU {
    static final int ROM_START = 0x200;

    private short[] stack = new short[16];
    private short stackPointer = 0;

    private final Memory memory;
    private final RegisterBlock registers;

    private short delayTimer = 0;
    private short soundTimer = 0;

    public void initialize(){
        loadFonts();
    }

    public CPU(Memory memory, RegisterBlock registers){
        this.memory = memory;
        this.registers = registers;
    }

    private void loadFonts(){
        //TODO: load fonts into memory locations [0-80]
    }

    public void loadRom(File rom) throws CPUException {
        try(FileInputStream fis = new FileInputStream(rom)){
            int nextByte = 0;
            int location = ROM_START;
            while((nextByte = fis.read()) >= 0){
                memory.write(location++, nextByte);
            }
        } catch (IOException | MemoryOutOfBoundsException ex){
            throw new CPUException("Failed to load ROM", ex);
        }
    }

    public void step() throws CPUException {
        execute(decode(fetch()));
        updateTimers();
    }

    private int fetch() throws CPUException{
        try {
            int pc = registers.getProgramCounter();
            int instruction = (memory.read(pc) << 8) + memory.read(pc + 1);
            return instruction;
        } catch (MemoryOutOfBoundsException ex){
            throw new CPUException("Exception reading from memory", ex);
        }
    }

    private int[] decode(int instruction){
        int operand = (instruction & 0x0FFFF);
        int opCode = (operand & 0x0F000) >> 12;
        return new int[]{opCode,operand};
    }

    private void execute(int[] decodedInstruction) throws CPUException{
        switch(decodedInstruction[0]){
            case 0x0:
                System.out.println("Bunch of options");
                break;
            case 0x1:
                jumpToAddress(decodedInstruction);
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
                loadVxRegister(decodedInstruction);
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
                loadIndexRegister(decodedInstruction);
                break;
            case 0xB:
                jumpToV0PlusNNN(decodedInstruction);
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
                throw new CPUException("Invalid opcode: "+Integer.toHexString(decodedInstruction[0]));
        }
    }

    private void loadVxRegister(int[] decodedInstruction) throws CPUException {
        int register = (decodedInstruction[1] & 0xF00) >> 8;
        int value = decodedInstruction[1] & 0xFF;
        registers.setVX(register, value);
        registers.incrementProgramCounter();
        System.out.println("Set V" + Integer.toHexString(register) + " to " + Integer.toHexString(value));
    }

    private void loadIndexRegister(int[] decodedInstruction) throws CPUException {
        int address = decodedInstruction[1] & 0xFFF;
        registers.setIndexRegister(address);
        registers.incrementProgramCounter();
        System.out.println("Set I to "+Integer.toHexString(address));
    }

    private void jumpToAddress(int[] decodedInstruction){
        int address = decodedInstruction[1];
        registers.jumpProgramCounter(address);
        System.out.println("Jump to "+Integer.toHexString(address));
    }

    private void jumpToV0PlusNNN(int[] decodedInstruction) throws CPUException{
        int address = decodedInstruction[1] & 0xFFF;
        int v0 = registers.getVX(0);
        registers.jumpProgramCounter(address + v0);
        System.out.println("Jump to V0 + NNN: " + Integer.toHexString(v0) + " + " + Integer.toHexString(address));
    }

    private void updateTimers(){
        //TODO: handle timers and figure out how to keep 60hz
    }

}
