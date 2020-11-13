package com.company.players;

import com.company.Board;
import com.company.IntPair;

import javax.swing.table.TableRowSorter;
import javax.xml.stream.FactoryConfigurationError;

public class Player95106437 extends Player {

    private int max_depth = 10;

    private boolean isValidMove(Board board, IntPair loc, int turn){
        return (loc.x>=0 && loc.x<20 && loc.y>=0 && loc.y<20) &&
                ((!(board.getCell(loc.x, loc.y).getColor() == turn)) &&
                (board.getCell(loc.x, loc.y).getColor() == 0 ||
                (board.getCell(loc.x, loc.y).getColor() == 3 - turn &&
                        board.getLength(turn) > board.getLength(3 - turn))));
    }

    public Player95106437(int col) {
        super(col);
    }

    private IntPair[] adjancts(IntPair location){
        IntPair[] adjs = new IntPair[4];
        adjs[0] = new IntPair(location.x-1, location.y);
        adjs[1] = new IntPair(location.x+1, location.y);
        adjs[2] = new IntPair(location.x, location.y-1);
        adjs[3] = new IntPair(location.x, location.y+1);
        return adjs;
    }


    private int evaluation(Board board){
        return board.getLength(2) - board.getLength(1);
    }

    private int[] max_value(Board board, int depth, int alpha, int betta){
        int[] arr = new int[3];
        if (depth == max_depth) {
            arr[0] = evaluation(board); arr[1] = alpha; arr[2] = betta;
            return arr;
        }
        int v = -100000;
        for (IntPair child:adjancts(board.getHead(2))){
            Board newBoard = new Board(board);

            if (isValidMove(board, child, 2)){
                int move_result = newBoard.move(child, 2);
                if (move_result != -1){
                    int[] temp = min_value(newBoard, depth+1, alpha, betta);
                    v = Math.max(v, temp[0]);
                    alpha = temp[1];
                    betta = temp[2];
                    if (v >= betta){
                        arr[0] = v; arr[1] = alpha; arr[2] = betta;
                        return arr;
                    }
                    betta = Math.min(betta, v);
                }
            }
        }
        arr[0] = v; arr[1] = alpha; arr[2] = betta;
        return arr;
    }

    private int[] min_value(Board board, int depth,int alpha,int betta){
        int[] arr = new int[3];
        if (depth == max_depth) {
            arr[0] = evaluation(board); arr[1] = alpha; arr[2] = betta;
            return arr;
        }
        int v = 100000;
        for (IntPair child:adjancts(board.getHead(1))){
            Board newBoard = new Board(board);
            if (isValidMove(board, child, 1)) {
                int move_result = newBoard.move(child, 1);
                if (move_result != -1) {
                    int[] temp = max_value(newBoard, depth+1, alpha, betta);
                    v = Math.min(v, temp[0]);
                    alpha = temp[1];
                    betta = temp[2];
                    if (v <= alpha){
                        arr[0] = v; arr[1] = alpha; arr[2] = betta;
                        return arr;
                    }
                    betta = Math.min(betta, v);
                }
            }
        }
        arr[0] = v; arr[1] = alpha; arr[2] = betta;
        return arr;
    }

    public IntPair getMove(Board board){
        int alpha = -200000, betta = 200000;
        IntPair loc0 = board.getHead(getCol());
        IntPair goal = new IntPair(loc0.x, loc0.y);
        if (getCol() == 1){
            int v = 100000;
            for (IntPair child:adjancts(board.getHead(getCol()))){
                Board newBoard = new Board(board);
                if (isValidMove(board, child, 1)) {
                    int move_result = newBoard.move(child, getCol());
                    if (move_result != -1) {
                        int[] value = max_value(newBoard, 1, alpha, betta);
                        if (v > value[0]) {
                            v = value[0];
                            goal = new IntPair(child);
                        }
                    }
                }
            }
        }else{
            int v = -100000;
            for (IntPair child:adjancts(board.getHead(getCol()))){
                Board newBoard = new Board(board);
                if (isValidMove(board, child, 2)) {
                    int move_result = newBoard.move(child, getCol());
                    if (move_result != -1) {
                        int[] value = min_value(newBoard, 1, alpha, betta);
                        if (v < value[0]) {
                            v = value[0];
                            goal = new IntPair(child);
                        }
                    }
                }
            }
        }
        System.out.println(getCol()+" --> "+goal.x+":"+goal.y);
        return goal;

    }

}
