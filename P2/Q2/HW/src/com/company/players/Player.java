package com.company.players;

import com.company.Board;
import com.company.IntPair;

/**
 * Created by The_CodeBreakeR on 3/16/17.
 */
public abstract class Player {

    private final int col;

    abstract public IntPair getMove(Board board);

    public Player(int col) {
        this.col = col;
    }

    public int getCol() {
        return col;
    }
}