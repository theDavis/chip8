package io.github.thedavis.chip8.cpu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRegisterBlock {

    @Test(expected=CPUException.class)
    public void testGetInvalidRegister_low() throws Exception{
        new RegisterBlock().getVX(-1);
    }

    @Test(expected=CPUException.class)
    public void testGetInvalidRegister_high() throws Exception{
        new RegisterBlock().getVX(16);
    }

    @Test(expected=CPUException.class)
    public void testSetInvalidRegister_low() throws Exception{
        new RegisterBlock().setVX(-1, 0);
    }

    @Test(expected=CPUException.class)
    public void testSetInvalidRegister_high() throws Exception{
        new RegisterBlock().setVX(16, 0);
    }

    @Test
    public void testSetAndGetRegister() throws Exception{
        final int index = 0;
        final int value = 0xFF;
        RegisterBlock register = new RegisterBlock();
        register.setVX(index, value);
        assertEquals(value, register.getVX(index));
    }

    @Test
    public void testSetGetIndexRegister_highValue(){
        final int value = 0xFFFF;
        RegisterBlock registers = new RegisterBlock();
        registers.setIndexRegister(value);
        assertEquals((value & 0xFFF), registers.getIndexRegister());
    }

    @Test
    public void testSetGetIndexRegister(){
        final int value = 0x111;
        RegisterBlock registers = new RegisterBlock();
        registers.setIndexRegister(value);
        assertEquals(value, registers.getIndexRegister());
    }
}