package io.github.thedavis.chip8.util;

public class Masker {

    static final int ADDRESS_MASK = 0xFFF;
    static final int INSTRUCTION_MASK = 0xFFFF;
    static final int OPCODE_MASK = 0xF000;
    static final int BYTE_MASK = 0xFF;
    static final int REGISTER_MASK = 0xF00;

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

    public static int maskRegisterAndShift(int instruction){
        return applyMask(instruction, REGISTER_MASK) >> 8;
    }

    private static int applyMask(int value, int mask){
        return value & mask;
    }
}
