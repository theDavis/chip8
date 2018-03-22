package io.github.thedavis.chip8.util;

public class Masker {

    static final int ADDRESS_MASK = 0xFFF;
    static final int INSTRUCTION_MASK = 0xFFFF;
    static final int OPCODE_MASK = 0xF000;
    static final int BYTE_MASK = 0xFF;

    public static int maskAddress(int address){
        return applyMask(address, ADDRESS_MASK);
    }

    public static int maskInstruction(int instruction){
        return applyMask(instruction, INSTRUCTION_MASK);
    }

    public static int maskOpCode(int instruction){
        return applyMask(instruction, OPCODE_MASK);
    }

    public static int maskByte(int value){
        return applyMask(value, BYTE_MASK);
    }

    private static int applyMask(int value, int mask){
        return value & mask;
    }
}
