package io.github.thedavis.chip8.cpu;

import io.github.thedavis.chip8.memory.Memory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestCPU {

    @Ignore("used to write ROM to memory to figure out instruction patterns")
    @Test
    public void test() throws Exception {
        Memory memory = new Memory();
        CPU cpu = new CPU(memory, new RegisterBlock());
        cpu.loadRom(new File("/home/thedavis/roms/chip8/PONG"));
        System.out.println("0000200: "+Integer.toHexString(memory.read(0x200)));
        System.out.println("0000201: "+Integer.toHexString(memory.read(0x201)));
    }

    @Test
    public void testLoadVxRegisterWithNN() throws Exception{
        Memory memory = new Memory();
        memory.write(0x200, 0x6A);
        memory.write(0x201, 0x02);

        RegisterBlock registers = new RegisterBlock();
        assertEquals(0, registers.getVX(0xA));

        new CPU(memory, registers).step();

        assertEquals(0x02, registers.getVX(0xA));
    }

    @Test
    public void testLoadIndexRegisterWithNNN() throws Exception {
        Memory memory = new Memory();
        memory.write(0x200, 0xAF);
        memory.write(0x201, 0xFF);

        RegisterBlock registers = new RegisterBlock();
        assertEquals(0, registers.getIndexRegister());

        new CPU(memory, registers).step();

        assertEquals(0xFFF, registers.getIndexRegister());
    }

    @Test
    public void testJumpToV0PlusNNN() throws Exception {
        final int v0 = 0x05;
        final int jumpValue = 0x0A;

        Memory memory = new Memory();
        memory.write(0x200, 0xB0);
        memory.write(0x201, jumpValue);

        RegisterBlock registers = new RegisterBlock();
        registers.setVX(0, v0);
        assertEquals(CPU.ROM_START, registers.getProgramCounter());

        new CPU(memory, registers).step();

        assertEquals(v0 + jumpValue, registers.getProgramCounter());
    }

}
