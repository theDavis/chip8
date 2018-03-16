package io.github.thedavis.chip8.memory;

public class MemoryOutOfBoundsException extends Exception{

    public MemoryOutOfBoundsException(String message){
        super(message);
    }

    public MemoryOutOfBoundsException(String message, Throwable t){
        super(message, t);
    }
}
