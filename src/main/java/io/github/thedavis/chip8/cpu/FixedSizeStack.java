package io.github.thedavis.chip8.cpu;

import java.util.ArrayDeque;

public class FixedSizeStack<T> extends ArrayDeque<T> {

    private final int maxSize;

    public FixedSizeStack(){
        this(0);
    }

    public FixedSizeStack(int maxSize){
        super();
        this.maxSize = maxSize;
    }

    @Override
    public void push(T object){
        while(this.size() >= maxSize){
            super.removeFirst();
        }

        super.push(object);
    }

}
