package io.github.thedavis.chip8.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMasker {

    private static final int INSTRUCTION = 0x1234;

    @Test
    public void testMaskAddress(){
        assertEquals(INSTRUCTION & 0xFFF, Masker.maskAddress(INSTRUCTION));
    }


    @Test
    public void testMaskByte() {
        assertEquals(INSTRUCTION & 0xFF, Masker.maskByte(INSTRUCTION));
    }

    @Test
    public void testMaskOpCode(){
        assertEquals(INSTRUCTION & 0xF000, Masker.maskOpCode(INSTRUCTION));
    }

    @Test
    public void testMaskInstruction(){
        assertEquals(INSTRUCTION & 0xFFFF, Masker.maskInstruction(INSTRUCTION));
    }
}
