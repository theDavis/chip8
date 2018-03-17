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
    public void testLoadVxRegister() throws Exception{
        Memory memory = new Memory();
        RegisterBlock registers = new RegisterBlock();
        memory.write(0x200, 0x6A);
        memory.write(0x201, 0x02);
        CPU cpu = new CPU(memory, registers);
        cpu.step();

        assertEquals(0x02, registers.getVX(0xA));
    }

}
