package io.github.thedavis.chip8.cpu;

import io.github.thedavis.chip8.memory.Memory;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestCPU {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testLoadRom() throws Exception {
        final byte[] romBytes = new byte[]{0x12, 0x34, 0x56, 0x78};

        File rom = new File(tempFolder.getRoot(), "TESTROM");
        try(FileOutputStream fout = new FileOutputStream(rom)){
            fout.write(romBytes);
        }

        Memory memory = new Memory();
        CPU cpu = new CPU(memory, new RegisterBlock());
        cpu.loadRom(rom);

        for(int i = 0; i < romBytes.length; i++){
            assertEquals((int) romBytes[i], (int) memory.read(i + CPU.ROM_START));
        }
    }

    @Test
    public void testJumpToNNN() throws Exception {
        Memory memory = new Memory();
        memory.write(0x200, 0x10);
        memory.write(0x201, 0x0A);

        RegisterBlock registers = new RegisterBlock();

        new CPU(memory, registers).step();

        assertEquals(0x00A, registers.getProgramCounter());
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

    @Test
    public void testCallSubroutine() throws Exception {
        final int subAddress = 0x20A;
        final int fullInstruction = 0x2000 | subAddress;

        Memory memory = new Memory();
        memory.write(0x200, (0xFF00 & fullInstruction) >> 8);
        memory.write(0x201, (0xFF & fullInstruction));
        memory.write(0x20A, 0x00);
        memory.write(0x20B, 0xEE);

        RegisterBlock registers = new RegisterBlock();

        CPU cpu = new CPU(memory, registers);

        cpu.step();

        assertEquals(subAddress, registers.getProgramCounter());
        assertEquals(CPU.ROM_START, (int) registers.getTopOfStack());

        cpu.step();

        assertEquals(CPU.ROM_START, registers.getProgramCounter());
        assertEquals(null, registers.getTopOfStack());
    }

    @Test
    public void testSkipIfVXEquals() throws Exception{
        final int register = 0x1;
        final int value = 0xFF;
        final int instruction = (0x3 << 12) + (register << 8) + value;

        RegisterBlock registers = new RegisterBlock();
        registers.setVX(register, value);

        Memory memory = new Memory();
        memory.write(CPU.ROM_START, (instruction & 0xFF00) >> 8);
        memory.write(CPU.ROM_START+1, (instruction & 0xFF));

        CPU cpu = new CPU(memory, registers);

        cpu.step();

        assertEquals(CPU.ROM_START+4, registers.getProgramCounter());
    }

    @Test
    public void testSkipIfVXEquals_false() throws Exception{
        final int register = 0x1;
        final int value = 0xFF;
        final int instruction = (0x3 << 12) + (register << 8) + value;

        RegisterBlock registers = new RegisterBlock();
        registers.setVX(register, 0x00);

        Memory memory = new Memory();
        memory.write(CPU.ROM_START, (instruction & 0xFF00) >> 8);
        memory.write(CPU.ROM_START+1, (instruction & 0xFF));

        CPU cpu = new CPU(memory, registers);

        cpu.step();

        assertEquals(CPU.ROM_START+2, registers.getProgramCounter());
    }

    @Test
    public void testSkipIfVXNotEquals() throws Exception {
        final int register = 0x1;
        final int value = 0xFF;
        final int instruction = (0x4 << 12) + (register << 8) + value;

        RegisterBlock registers = new RegisterBlock();
        registers.setVX(register, 0x00);

        Memory memory = new Memory();
        memory.write(CPU.ROM_START, (instruction & 0xFF00) >> 8);
        memory.write(CPU.ROM_START+1, (instruction & 0xFF));

        CPU cpu = new CPU(memory, registers);
        cpu.step();

        assertEquals(CPU.ROM_START+4, registers.getProgramCounter());
    }

    @Test
    public void testSkipIfVXNotEquals_false() throws Exception {
        final int register = 0x1;
        final int value = 0xFF;
        final int instruction = (0x4 << 12) + (register << 8) + value;

        RegisterBlock registers = new RegisterBlock();
        registers.setVX(register, 0xFF);

        Memory memory = new Memory();
        memory.write(CPU.ROM_START, (instruction & 0xFF00) >> 8);
        memory.write(CPU.ROM_START+1, (instruction & 0xFF));

        CPU cpu = new CPU(memory, registers);
        cpu.step();

        assertEquals(CPU.ROM_START+2, registers.getProgramCounter());
    }

    @Test
    public void testSkipIfVXEqualsVY() throws Exception {
        final int registerX = 0x1;
        final int registerY = 0x2;
        final int instruction = (0x5 << 12) + (registerX << 8) + (registerY << 4);

        RegisterBlock registers = new RegisterBlock();
        registers.setVX(registerX, 0xFF);
        registers.setVX(registerY, 0xFF);

        Memory memory = new Memory();
        memory.write(CPU.ROM_START, (instruction & 0xFF00) >> 8);
        memory.write(CPU.ROM_START+1, (instruction & 0xFF));

        CPU cpu = new CPU(memory, registers);
        cpu.step();

        assertEquals(CPU.ROM_START+4, registers.getProgramCounter());
    }

    @Test
    public void testSkipIfVXEqualsVY_false() throws Exception {
        final int registerX = 0x1;
        final int registerY = 0x2;
        final int instruction = (0x5 << 12) + (registerX << 8) + (registerY << 4);

        RegisterBlock registers = new RegisterBlock();
        registers.setVX(registerX, 0xFF);
        registers.setVX(registerY, 0x55);

        Memory memory = new Memory();
        memory.write(CPU.ROM_START, (instruction & 0xFF00) >> 8);
        memory.write(CPU.ROM_START+1, (instruction & 0xFF));

        CPU cpu = new CPU(memory, registers);
        cpu.step();

        assertEquals(CPU.ROM_START+2, registers.getProgramCounter());
    }

    @Test
    public void testAddNNToVX_no_overflow() throws Exception {
        final int register = 0x4;
        final int value = 0x10;
        final int instruction = (0x7 << 12) + (register << 8) + value;

        RegisterBlock registers = new RegisterBlock();
        registers.setVX(register, value);

        Memory memory = new Memory();
        memory.write(CPU.ROM_START, (instruction & 0xFF00) >> 8);
        memory.write(CPU.ROM_START+1, (instruction & 0xFF));

        CPU cpu = new CPU(memory, registers);
        cpu.step();

        assertEquals(value*2, registers.getVX(register));
    }

    @Test
    public void testAddNNToVX_with_overflow() throws Exception {
        final int register = 0x6;
        final int value = 0xFF;
        final int instruction = (0x7 << 12) + (register << 8) + value;

        RegisterBlock registers = new RegisterBlock();
        registers.setVX(register, 0x01);

        Memory memory = new Memory();
        memory.write(CPU.ROM_START, (instruction & 0xFF00) >> 8);
        memory.write(CPU.ROM_START+1, (instruction & 0xFF));

        CPU cpu = new CPU(memory, registers);
        cpu.step();

        assertEquals(0x00, registers.getVX(register));
        assertEquals(0x0, registers.getVX(0xF));
    }

}
