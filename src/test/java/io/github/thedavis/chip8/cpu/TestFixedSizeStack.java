package io.github.thedavis.chip8.cpu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFixedSizeStack {

    @Test
    public void testExceedMaxSize(){
        final int maxSize = 1;
        FixedSizeStack<Integer> stack = new FixedSizeStack<>(maxSize);

        assertEquals(0, stack.size());
        stack.push(0);
        assertEquals(1, stack.size());
        assertEquals(0, (int) stack.peek());
        stack.push(1);
        assertEquals(1, stack.size());
        assertEquals(1, (int) stack.peek());
    }
}
