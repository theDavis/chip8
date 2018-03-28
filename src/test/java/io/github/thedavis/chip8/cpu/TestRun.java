package io.github.thedavis.chip8.cpu;

import io.github.thedavis.chip8.memory.Memory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class TestRun {

    @Ignore
    @Test
    public void test() throws Exception {
        CPU cpu = new CPU(new Memory(), new RegisterBlock());
        cpu.loadRom(new File("/home/thedavis/roms/chip8/PONG"));
        for(int i = 0; i < 100; i++){
            cpu.step();
        }
    }
}
