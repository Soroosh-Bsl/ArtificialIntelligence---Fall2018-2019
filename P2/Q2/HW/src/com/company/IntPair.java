package com.company;

/**
 * Created by The_CodeBreakeR on 2/20/18.
 */
public class IntPair {
    public final int x;
    public final int y;

    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IntPair(IntPair intp) {
        this.x = intp.x;
        this.y = intp.y;
    }
}