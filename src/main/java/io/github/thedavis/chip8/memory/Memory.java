package io.github.thedavis.chip8.memory;

public class Memory {

    private static final int MEMORY_SIZE = 4096;

    private final short[] memory;

    public Memory(){
        memory = new short[MEMORY_SIZE];
    }

    public void dumpMemory() throws MemoryOutOfBoundsException{
        for(int i = 0; i < memory.length; i = i+2){
            int current = read(i);
            current = current << 8;
            current += read(i+1);
            System.out.println(Integer.toHexString(current));
        }
    }

    public void write(int location, int value) throws MemoryOutOfBoundsException{
        if(location >= 0 && location <= MEMORY_SIZE) {
            memory[location] = getSingleByte(value);
        } else {
            throw new MemoryOutOfBoundsException("Access outside range: "+location);
        }
    }

    public short read(int location) throws MemoryOutOfBoundsException{
        if(location >= 0 && location <= MEMORY_SIZE) {
            return getSingleByte(memory[location]);
        } else {
            throw new MemoryOutOfBoundsException("Access outside range: "+location);
        }
    }

    private short getSingleByte(int value){
        return (short) (value & 0xFF);
    }
}
