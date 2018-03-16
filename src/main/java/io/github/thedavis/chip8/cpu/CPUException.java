package io.github.thedavis.chip8.cpu;

public class CPUException extends Exception {

    public CPUException(String message){
        super(message);
    }

    public CPUException(String message, Throwable t){
        super(message, t);
    }
}
