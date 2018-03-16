package io.github.thedavis.chip8.memory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMemory {

    @Test(expected=MemoryOutOfBoundsException.class)
    public void testWrite_BelowRange() throws Exception{
        new Memory().write(-1, 0xFF);
    }

    @Test(expected=MemoryOutOfBoundsException.class)
    public void testWrite_AboveRange() throws Exception{
        new Memory().write(4097, 1);
    }

    @Test
    public void testWriteRead() throws Exception {
        final int location = 0x200;
        final int value = 0xFF;
        Memory memory = new Memory();
        memory.write(location, value);
        assertEquals(memory.read(location), value);
    }

    @Test(expected=MemoryOutOfBoundsException.class)
    public void testRead_belowRange() throws Exception {
        new Memory().read(-1);
    }

    @Test(expected=MemoryOutOfBoundsException.class)
    public void testRead_aboveRange() throws Exception {
        new Memory().read(4097);
    }
}
