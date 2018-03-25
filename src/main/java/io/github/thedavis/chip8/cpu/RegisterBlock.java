package io.github.thedavis.chip8.cpu;

import io.github.thedavis.chip8.util.Masker;

import java.util.ArrayDeque;
import java.util.Deque;

public class RegisterBlock {

    private static final int MAX_STACK_SIZE = 16;

    private final short[] registers;
    private int indexRegister = 0;
    private int programCounter = CPU.ROM_START;
    private final Deque<Integer> stack;

    public RegisterBlock(){
        registers = new short[16];
        stack = new FixedSizeStack<>(16);
    }

    public short getVX(int index) throws CPUException {
        if(index >=0 && index <= 15) {
            return (short) Masker.maskByte(registers[index]);
        } else {
            throw new CPUException("Register out of range: " + index);
        }
    }

    public void setVX(int index, int value) throws CPUException {
        if(index >= 0 && index <= 15) {
            registers[index] = (short) Masker.maskByte(value);
        } else {
            throw new CPUException("Register out of range: " + index);
        }
    }

    public int getIndexRegister(){
        return indexRegister;
    }

    public void setIndexRegister(int address){
        indexRegister = Masker.maskAddress(address);
    }

    public int getProgramCounter(){
        return programCounter;
    }

    public void jumpProgramCounter(int address){
        stack.push(programCounter);
        programCounter = Masker.maskAddress(address);
    }

    public void incrementProgramCounter(){
        programCounter += 2;
    }

    int getTopOfStack(){
        return stack.peek();
    }

}
