package com.company.players;

import com.company.Board;
import com.company.IntPair;
import com.sun.scenario.effect.impl.prism.PrImage;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.ArrayList;

public class Player95103175 extends Player {

    public Player95103175(int col) { super(col); }

    @Override
    public IntPair getMove(Board board) {

        Node max = maxValue(board,board,-1000,1000,8);

//        if (max != null){ System.out.println(max.value);}

        int currentX = board.getHead(this.getCol()).x;
        int currentY = board.getHead(this.getCol()).y;

        if (max == null) {
            if (currentX+1 != 20 && board.getCell(currentX+1,currentY).getColor() == 0) {
                return new IntPair(currentX+1,currentY);
            }
            else if (currentX-1 != -1 && board.getCell(currentX-1,currentY).getColor() == 0) {
                return new IntPair(currentX-1,currentY);
            }
            else if (currentY+1 != 20 && board.getCell(currentX,currentY+1).getColor() == 0) {
                return new IntPair(currentX,currentY+1);
            }
            else if (currentY-1 != -1 && board.getCell(currentX+1,currentY).getColor() == 0 ) {
                return new IntPair(currentX,currentY-1);
            }
            else {
                return new IntPair(0,0);
            }
        }

        return new IntPair(max.place.x + currentX,max.place.y + currentY);
    }

    private int evaluation(Board superBoard, Board board) {
        int mySpanMovememt = 0;
        int opponentSpanMovement = 0;
        int myDistanceToCenter = 0;
        int opponentDistanceTocenter = 0;
        int myLength = 0;
        int opponentLength = 0;

        int myColor = this.getCol();
        int opponentColor = 0;

        if (myColor == 1) { opponentColor = 2; }
        else { opponentColor = 1; }

        mySpanMovememt += isPossible(board,myColor,new IntPair(0,-1)) +
                isPossible(board,myColor,new IntPair(0,1)) +
                isPossible(board,myColor,new IntPair(1,0)) +
                isPossible(board,myColor,new IntPair(-1,-0));

        opponentSpanMovement += isPossible(board,opponentColor,new IntPair(0,-1)) +
                isPossible(board,opponentColor,new IntPair(0,1)) +
                isPossible(board,opponentColor,new IntPair(1,0)) +
                isPossible(board,opponentColor,new IntPair(-1,-0));


        myDistanceToCenter += Math.abs(board.getHead(myColor).x - 10) +
                Math.abs(board.getHead(myColor).y - 10);

        opponentDistanceTocenter += Math.abs(board.getHead(opponentColor).x - 10) +
                Math.abs(board.getHead(opponentColor).y - 10);

        myLength = board.getLength(myColor) -  superBoard.getLength(myColor);
        opponentLength = board.getLength(opponentColor) - superBoard.getLength(opponentColor);

        System.out.println(myLength);
        System.out.println(opponentLength);
        System.out.println("---------------------------");
        return opponentDistanceTocenter - myDistanceToCenter +
                mySpanMovememt * 100 - opponentSpanMovement*50 +
                myLength * 40 - opponentLength * 20;
    }

    private Node maxValue (Board superBoard,Board board, int alpha, int beta,int depth) {

        if (depth == 0) { return new Node(null,evaluation(superBoard, board)); }

        int color = this.getCol();

        ArrayList<IntPair> moves = children(board,color);

        Node max = null;

        for (IntPair move : moves){
            Board next = new Board(board);
            IntPair iterator = new IntPair(move.x + board.getHead(color).x,move.y + board.getHead(color).y);
            next.move(iterator,color);
            Node min = minValue(superBoard, next,alpha,beta,depth -1 );

            if (min != null && ((max == null) || min.value > max.value)) { max = new Node(move,min.value); }

            if (max != null && max.value > alpha) { alpha = max.value; }

            if (beta <= alpha) { break; }
        }
        return max;
    }

    private Node minValue (Board superBoard, Board board, int alpha, int beta, int depth) {

        if (depth == 0) { return new Node(null,evaluation(superBoard, board)); }

        int color = this.getCol();

        if (color == 1) { color = 2; }
        else { color = 1; }

        ArrayList<IntPair> moves = children(board,color);

        Node min = null;

        for (IntPair move : moves) {
            Board next = new Board(board);
            IntPair iterator = new IntPair(move.x + board.getHead(color).x,move.y + board.getHead(color).y);
            next.move(iterator,color);
            Node max = maxValue(superBoard, next,alpha,beta,depth-1);

            if (max != null && ((min == null) || min.value > max.value)) { min = new Node(move,max.value); }

            if (min != null && min.value < beta) { beta = min.value; }

            if (beta <= alpha) { break; }
        }
        return min;
    }

    private int isPossible(Board board, int col, IntPair move) {
        int plc_x = board.getHead(col).x;
        int plc_y = board.getHead(col).y;
        IntPair destination = new IntPair(plc_x + move.x,plc_y+move.y);
        if (destination.x > -1 && destination.x < 20 && destination.y > -1 && destination.y < 20 &&
                board.getCell(destination.x,destination.y).getColor() == 0) {
            return 1;
        }
        return 0;
    }

    private ArrayList<IntPair> children(Board board,int col) {
        ArrayList<IntPair> children = new ArrayList<>();
        IntPair test1 = new IntPair(0,1);
        if (isPossible(board,col,test1) == 1) { children.add(test1); }

        IntPair test2 = new IntPair(0,-1);
        if (isPossible(board,col,test2) == 1) { children.add(test2); }

        IntPair test3 = new IntPair(1,0);
        if (isPossible(board,col,test3) == 1) { children.add(test3); }

        IntPair test4 = new IntPair(-1,0);
        if (isPossible(board,col,test4) == 1) { children.add(test4); }

        return children;
    }

    class Node {
        IntPair place;
        int value;

        Node(IntPair place, int value){
            this.value = value;
            this.place = place;
        }
    }
}
