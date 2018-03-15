package io.github.thedavis.chip8;

import java.io.File;
import java.io.IOException;

public class TestCPU {

    public static void main(String[] args) throws IOException {
        CPU cpu = new CPU();
        File file = new File("/home/thedavis/roms/chip8/PONG");
        cpu.loadRom(file);

        for(int i = 0; i < 10; i++){
            cpu.step();
        }
    }
}
