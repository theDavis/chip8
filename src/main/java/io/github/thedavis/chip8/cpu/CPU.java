package io.github.thedavis.chip8.cpu;

import io.github.thedavis.chip8.memory.Memory;
import io.github.thedavis.chip8.memory.MemoryOutOfBoundsException;
import io.github.thedavis.chip8.util.Masker;

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
        int maskedInstruction = Masker.maskInstruction(instruction);
        int opCode = Masker.maskOpCode(instruction) >> 12;
        return new int[]{opCode,maskedInstruction};
    }

    private void execute(int[] decodedInstruction) throws CPUException{
        switch(decodedInstruction[0]){
            case 0x0:
                if(0xEE == (decodedInstruction[1])){
                    returnFromSubroutine();
                } else {
                    //TODO: 0x00E0 clear screen
                    registers.incrementProgramCounter();
                    System.out.println("clear screen");
                }
                break;
            case 0x1:
                jumpToAddress(decodedInstruction);
                break;
            case 0x2:
                callSubroutine(decodedInstruction);
                break;
            case 0x3:
                skipIfVXEquals(decodedInstruction);
                break;
            case 0x4:
                skipIfVXNotEquals(decodedInstruction);
                break;
            case 0x5:
                skipIfVXEqualsVY(decodedInstruction);
                break;
            case 0x6:
                loadVxRegister(decodedInstruction);
                break;
            case 0x7:
                addNNToVX(decodedInstruction);
                break;
            case 0x8:
                //TODO: 8XY0 set VX = VY
                //TODO: 8XY1 set VX = VX | VY
                //TODO: 8XY2 set VX = VX & VY
                //TODO: 8XY3 set VX = VX ^ VY
                //TODO: 8XY4 set VX += VY (VF=1 if carry else VF=0)
                //TODO: 8XY5 set VX -= VY (VF=0 if borrow else VF=1)
                //TODO: 8XY6 set VX = VY >> 1 (VF = VY's least significant bit)
                //TODO: 8XY7 set VX = VY-VX (VF=0 if borrow else VF=1)
                //TODO: 8XYE set VX = VY << 1 (VF = VY's most significant bit)
                System.out.println("Bunch of VX operators");
                registers.incrementProgramCounter();
                break;
            case 0x9:
                //TODO: 9XY0 skip if (VX != VY)
                System.out.println("Skip next if Vx != Vy");
                registers.incrementProgramCounter();
                break;
            case 0xA:
                loadIndexRegister(decodedInstruction);
                break;
            case 0xB:
                jumpToV0PlusNNN(decodedInstruction);
                break;
            case 0xC:
                //TODO: CXNN set VX=rand() & NN where rand()=0-255
                System.out.println("Random");
                registers.incrementProgramCounter();
                break;
            case 0xD:
                //TODO: DXYN Draw
                System.out.println("Draw");
                registers.incrementProgramCounter();
                break;
            case 0xE:
                //TODO: EX9E skip next if(key() == VX)
                //TODO: EXA1 skip next if(key() != VX)
                System.out.println("Bunch of keypress things");
                registers.incrementProgramCounter();
                break;
            case 0xF:
                //TODO: FX07 set VX = delay timer
                //TODO: FX0A set VX = get_key() (blocking)
                //TODO: FX15 set delay_timer = VX
                //TODO: FX18 set sound_timer = VX
                //TODO: FX1E I += VX
                //TODO: FX29 I = sprite_addr[VX]
                //TODO: FX33 BCD?
                //TODO: FX55 dump registers to memory starting at I
                //TODO: FX65 load registers from memory starting at I
                System.out.println("Bunch of timer/memory/index register things");
                registers.incrementProgramCounter();
                break;
            default:
                throw new CPUException("Invalid opcode: "+Integer.toHexString(decodedInstruction[0]));
        }
    }

    private void returnFromSubroutine() throws CPUException {
        registers.returnFromSubroutine();
        System.out.println("Return from subroutine");
    }

    private void jumpToAddress(int[] decodedInstruction){
        int address = Masker.maskAddress(decodedInstruction[1]);
        registers.jumpProgramCounter(address);
        System.out.println("Jump to "+Integer.toHexString(address));
    }

    private void callSubroutine(int[] decodedInstruction){
        int address = Masker.maskAddress(decodedInstruction[1]);
        registers.callSubroutine(address);
        System.out.println("Call sub at "+Integer.toHexString(address));
    }

    private void skipIfVXEquals(int[] decodedInstruction) throws CPUException{
        int register = Masker.maskRegisterAndShift(decodedInstruction[1]);
        int value = Masker.maskByte(decodedInstruction[1]);
        if(registers.getVX(register) == value){
            registers.incrementProgramCounter();
        }
        registers.incrementProgramCounter();
        System.out.println("Skip if VX == NN " + Integer.toHexString(decodedInstruction[1]));
    }

    private void skipIfVXNotEquals(int[] decodedInstruction) throws CPUException{
        int register = Masker.maskRegisterAndShift(decodedInstruction[1]);
        int value = Masker.maskByte(decodedInstruction[1]);
        if(registers.getVX(register) != value){
            registers.incrementProgramCounter();
        }
        registers.incrementProgramCounter();
        System.out.println("Skip if VX != NN " + Integer.toHexString(decodedInstruction[1]));
    }

    private void skipIfVXEqualsVY(int[] decodedInstruction) throws CPUException{
        int registerX = (decodedInstruction[1] & 0xF00) >> 8;
        int registerY = (decodedInstruction[1] & 0xF0) >> 4;
        if(registers.getVX(registerX) == registers.getVX(registerY)){
            registers.incrementProgramCounter();
        }
        registers.incrementProgramCounter();
        System.out.println("Skip if VX == VY " + Integer.toHexString(decodedInstruction[1]));
    }

    private void loadVxRegister(int[] decodedInstruction) throws CPUException {
        int register = (decodedInstruction[1] & 0xF00) >> 8;
        int value = Masker.maskByte(decodedInstruction[1]);
        registers.setVX(register, value);
        registers.incrementProgramCounter();
        System.out.println("Set V" + Integer.toHexString(register) + " to " + Integer.toHexString(value));
    }

    private void addNNToVX(int[] decodedInstruction) throws CPUException {
        int register = (decodedInstruction[1] & 0xF00) >> 8;
        int value = Masker.maskByte(decodedInstruction[1]);
        int sum = (registers.getVX(register) + value) & 0xFF;

        registers.setVX(register, sum);
        registers.incrementProgramCounter();
        System.out.println("Add NN to VX " + Integer.toHexString(register));
    }

    private void loadIndexRegister(int[] decodedInstruction) throws CPUException {
        int address = Masker.maskAddress(decodedInstruction[1]);
        registers.setIndexRegister(address);
        registers.incrementProgramCounter();
        System.out.println("Set I to "+Integer.toHexString(address));
    }

    private void jumpToV0PlusNNN(int[] decodedInstruction) throws CPUException{
        int address = Masker.maskAddress(decodedInstruction[1]);
        int v0 = registers.getVX(0);
        registers.jumpProgramCounter(address + v0);
        System.out.println("Jump to V0 + NNN: " + Integer.toHexString(v0) + " + " + Integer.toHexString(address));
    }

    private void updateTimers(){
        //TODO: handle timers and figure out how to keep 60hz
    }

}
