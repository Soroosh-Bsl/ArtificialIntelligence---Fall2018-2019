package com.company.players;

import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

/**
 * Created by lenovo on 11/8/2018.
 */

public class Player95105408 extends Player {
    private static int depth = 8;

    private class Solution {
        int value;
        IntPair move;

        Solution(int value, IntPair move) {
            this.value = value;
            this.move = move;
        }
    }

    public Player95105408(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        Solution s = minimax(this.getCol(), 0, true, -100000, 100000, board);
        return s.move;
    }

    private Solution minimax(int colour, int depth, boolean maximizer, int alpha, int beta, Board board) {
        if (depth == getDepth()) {
            return new Solution(evaluate(board), board.getHead(colour));
        }
        if (maximizer) {
            ArrayList neighbours = neighbours(colour, board.getHead(colour), board);
            int best = -10000;
            IntPair bestMove = new IntPair(board.getHead(colour).x, board.getHead(colour).y);
            for (int i = 0; i < neighbours.size(); i++) {
                Board tmp = new Board(board);
                tmp.move((IntPair) neighbours.get(i), colour);
                Solution value = minimax(3 - colour, depth + 1, false, alpha, beta, tmp);
                if (value.value > best) {
                    best = value.value;
                    bestMove = (IntPair) (neighbours.get(i));
                    if (best > beta) {
                        break;
                    }
                    if (alpha < best) {
                        alpha = best;
                    }
                }
            }
            return new Solution(best, bestMove);
        } else {
            ArrayList neighbours = neighbours(colour, board.getHead(colour), board);
            int best = 10000;
            IntPair bestMove = new IntPair(0, 0);
            for (int i = 0; i < neighbours.size(); i++) {
                Board tmp = new Board(board);
                Solution value = minimax(3 - colour, depth + 1, true, alpha, beta, tmp);
                if (value.value < best) {
                    best = value.value;
                    bestMove = value.move;
                    if (best < alpha) {
                        break;
                    }
                    if (beta > best) {
                        beta = best;
                    }
                }
            }
            return new Solution(best, bestMove);
        }
    }

    private int evaluate(Board board) {
        int numberOfMyMoves = neighbours(getCol(), board.getHead(getCol()), board).size();
        int numberOfOpponentMoves = neighbours(3 - getCol(), board.getHead(3 - getCol()), board).size();
        int opY_distance_from_center = Math.abs(10 - board.getHead(3 - getCol()).y);
        int opX_distance_from_center = Math.abs(10 - board.getHead(3 - getCol()).x);
        int myY_distance_from_center = Math.abs(10 - board.getHead(getCol()).y);
        int myX_distance_from_center = Math.abs(10 - board.getHead(getCol()).x);
        int op_distance_from_center = opX_distance_from_center + opY_distance_from_center;
        int my_distance_from_center = myX_distance_from_center + myY_distance_from_center;

        int myAroundBalls = 2 * numberOfBalls(getCol(), 2, board) + numberOfBalls(getCol(), 1, board);

        return (int) (0.5 * (myAroundBalls) + 0.3*(1.5*op_distance_from_center - my_distance_from_center) + 36 * (2 * board.getLength(getCol()) - board.getLength(3 - getCol())) + 60 * (3 * numberOfMyMoves - 1.3 * numberOfOpponentMoves));
    }

    private int getDepth() {
        return depth;
    }

    static private ArrayList neighbours(int color, IntPair head, Board board) {
        ArrayList<IntPair> neighbours = new ArrayList<>();
        if (head.x - 1 >= 0 && (board.getLength(color) > board.getLength(3 - color) || board.getCell(head.x - 1, head.y).getColor() != 3 - color) && board.getCell(head.x - 1, head.y).getColor() != color) {
            neighbours.add(new IntPair(head.x - 1, head.y));
        }
        if (head.y - 1 >= 0 && (board.getLength(color) > board.getLength(3 - color) || board.getCell(head.x, head.y - 1).getColor() != 3 - color) && board.getCell(head.x, head.y - 1).getColor() != color) {
            neighbours.add(new IntPair(head.x, head.y - 1));
        }
        if (head.x + 1 <= 19 && (board.getLength(color) > board.getLength(3 - color) || board.getCell(head.x + 1, head.y).getColor() != 3 - color) && board.getCell(head.x + 1, head.y).getColor() != color) {
            neighbours.add(new IntPair(head.x + 1, head.y));
        }
        if (head.y + 1 <= 19 && (board.getLength(color) > board.getLength(3 - color) || board.getCell(head.x, head.y + 1).getColor() != 3 - color) && board.getCell(head.x, head.y + 1).getColor() != color) {
            neighbours.add(new IntPair(head.x, head.y + 1));
        }
        return neighbours;
    }

    private int numberOfBalls(int color, int ball, Board board) {
        int counter = 0;
        for (int i = board.getHead(color).x - 2; i <= board.getHead(color).x + 2; i++) {
            if (i >= 0 && i <= 19) {
                for (int j = board.getHead(color).y - 2; j <= board.getHead(color).y + 2; j++) {
                    if (j >= 0 && j <= 19) {
                        if (board.getCellValues(i, j) == ball) {
                            counter += 1;
                        }
                    }
                }
            }
        }
        return counter;
    }
}
