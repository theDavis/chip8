package io.github.thedavis.chip8.cpu;

public class RegisterBlock {

    private final short[] registers;
    private int indexRegister = 0;
    private int programCounter = CPU.ROM_START;

    public RegisterBlock(){
        registers = new short[16];
    }

    public short getVX(int index) throws CPUException {
        if(index >=0 && index <= 15) {
            return (short) (registers[index] & 0xFF);
        } else {
            throw new CPUException("Register out of range: " + index);
        }
    }

    public void setVX(int index, int value) throws CPUException {
        if(index >= 0 && index <= 15) {
            registers[index] = (short) (value & 0xFF);
        } else {
            throw new CPUException("Register out of range: " + index);
        }
    }

    public int getIndexRegister(){
        return indexRegister;
    }

    public void setIndexRegister(int value){
        indexRegister = (value & 0xFFF);
    }

    public int getProgramCounter(){
        return programCounter;
    }

    public void jumpProgramCounter(int address){
        programCounter = (address & 0xFFF);
    }

    public void incrementProgramCounter(){
        programCounter += 2;
    }
}
