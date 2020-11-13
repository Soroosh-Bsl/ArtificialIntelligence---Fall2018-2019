package com.company.players;

import com.company.Board;
import com.company.IntPair;

import java.util.concurrent.ThreadLocalRandom;

public class NaivePlayer extends Player {

    public NaivePlayer(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        IntPair h1 = board.getHead(getCol());
        while (true) {
            int n = ThreadLocalRandom.current().nextInt(0, 4);
            if (n == 0) {
                if (h1.y < 19) {
                    if (board.getCell(h1.x, h1.y + 1).getColor() == 0 ||
                            (board.getCell(h1.x, h1.y + 1).getColor() == 3 - getCol() &&
                                    board.getLength(getCol()) > board.getLength(3 - getCol())))
                        return new IntPair(h1.x, h1.y + 1);
                }
            } else if (n == 1) {
                if (h1.y > 0) {
                    if (board.getCell(h1.x, h1.y - 1).getColor() == 0 ||
                            (board.getCell(h1.x, h1.y - 1).getColor() == 3 - getCol() &&
                                    board.getLength(getCol()) > board.getLength(3 - getCol())))
                        return new IntPair(h1.x, h1.y - 1);
                }
            } else if (n == 2) {
                if (h1.x < 19) {
                    if (board.getCell(h1.x + 1, h1.y).getColor() == 0 ||
                            (board.getCell(h1.x + 1, h1.y).getColor() == 3 - getCol() &&
                                    board.getLength(getCol()) > board.getLength(3 - getCol())))
                        return new IntPair(h1.x + 1, h1.y);
                }
            } else {
                if (h1.x > 0) {
                    if (board.getCell(h1.x - 1, h1.y).getColor() == 0 ||
                            (board.getCell(h1.x - 1, h1.y).getColor() == 3 - getCol() &&
                                    board.getLength(getCol()) > board.getLength(3 - getCol())))
                        return new IntPair(h1.x - 1, h1.y);
                }
            }
        }
    }
}
