package com.company;

import java.util.concurrent.ThreadLocalRandom;

public class Board {

    public final static int size = 20;
    private final static int maxNumberOfMoves = 500;
    private Cell[][] cells = new Cell[size][size];
    private int[][] cellValues = new int[size][size];
    private LinkedList head[] = new LinkedList[2];
    private LinkedList tail[] = new LinkedList[2];
    private int counter[] = new int[2];
    private int length[] = new int[2];
    private int numberOfMoves = 0;

    public Board() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.cells[i][j] = new Cell(i, j, 0);
        this.cells[size / 3 - 1][size / 3 - 1] = new Cell(size / 3 - 1, size / 3 - 1, 1);
        this.cells[2 * size / 3][2 * size / 3] = new Cell(2 * size / 3, 2 * size / 3, 2);
        this.head[0] = new LinkedList(new IntPair(size / 3 - 1, size / 3 - 1), null);
        this.tail[0] = this.head[0];
        this.head[1] = new LinkedList(new IntPair(2 * size / 3, 2 * size / 3), null);
        this.tail[1] = this.head[1];
        this.length[0] = 1;
        this.length[1] = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cellValues[i][j] = ThreadLocalRandom.current().nextInt(0, 13);
                if (cellValues[i][j] <= 8) {
                    cellValues[i][j] = 0;
                } else if (cellValues[i][j] <= 11) {
                    cellValues[i][j] = 1;
                } else {
                    cellValues[i][j] = 2;
                }

            }
        }
    }

    public Board(Board board) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.cells[i][j] = new Cell(board.cells[i][j]);
        LinkedList temp = board.getTailLinkedList(1);
        this.tail[0] = new LinkedList(board.getTailLinkedList(1));
        this.head[0] = this.tail[0];
        while (temp.getNext() != null) {
            this.head[0].setNext(new LinkedList(temp.getNext()));
            this.head[0] = this.head[0].getNext();
            temp = temp.getNext();
        }
        temp = board.getTailLinkedList(2);
        this.tail[1] = new LinkedList(board.getTailLinkedList(2));
        this.head[1] = this.tail[1];
        while (temp.getNext() != null) {
            this.head[1].setNext(new LinkedList(temp.getNext()));
            this.head[1] = this.head[1].getNext();
            temp = temp.getNext();
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cellValues[i][j] = board.getCellValues(i, j);
            }
        }
        this.length[0] = board.getLength(1);
        this.length[1] = board.getLength(2);
        this.counter[0] = board.getCounter(1);
        this.counter[1] = board.getCounter(2);
        this.numberOfMoves = board.getNumberOfMoves();
    }

    public Cell getCell(int i, int j) {
        return cells[i][j];
    }

    public IntPair getHead(int playerColor) {
        return head[playerColor - 1].getIntPair();
    }

    public IntPair getTail(int playerColor) {
        return tail[playerColor - 1].getIntPair();
    }

    public LinkedList getTailLinkedList(int playerColor) {
        return tail[playerColor - 1];
    }

    public int getLength(int playerColor) {
        return length[playerColor - 1];
    }

    public int getCounter(int playerColor) {
        return counter[playerColor - 1];
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    int win() {
        return 0;
    }

    public int move(IntPair cellNum, int playerColor) {
        numberOfMoves++;
        if (cellNum.x < 0 || cellNum.x > size - 1 || cellNum.y < 0 || cellNum.y > size - 1 ||
                !adj(head[playerColor - 1].getIntPair(), cellNum) || cells[cellNum.x][cellNum.y].getColor()
                == playerColor || cells[cellNum.x][cellNum.y].getColor() == 3) {
            return -1;
        } else if (cells[cellNum.x][cellNum.y].getColor() != 0) {
            if (length[playerColor - 1] <= length[cells[cellNum.x][cellNum.y].getColor() - 1]) {
                return -1;
            } else {
                for (int i = 0; i < length[2 - playerColor]; i++) {
                    cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y]
                            .setColor(cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y]
                                    .getColor() - playerColor);
                    if (cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y].getColor() < 0)
                        cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y].setColor(0);
                    tail[playerColor - 1] = tail[playerColor - 1].getNext();
                }
                length[playerColor - 1] -= length[cells[cellNum.x][cellNum.y].getColor() - 1];
                LinkedList temp = new LinkedList(cellNum, null);
                head[playerColor - 1].setNext(temp);
                head[playerColor - 1] = temp;
                cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y]
                        .setColor(cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y]
                                .getColor() - playerColor);
                tail[playerColor - 1] = tail[playerColor - 1].getNext();
                cells[cellNum.x][cellNum.y].setColor(cells[cellNum.x][cellNum.y].getColor() + playerColor);
            }
        } else {
            counter[playerColor - 1] += cellValues[cellNum.x][cellNum.y];
            cells[cellNum.x][cellNum.y].setColor(playerColor + cells[cellNum.x][cellNum.y].getColor());
            LinkedList temp = new LinkedList(cellNum, null);
            head[playerColor - 1].setNext(temp);
            head[playerColor - 1] = temp;
            if (counter[playerColor - 1] == 0) {
                cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y].setColor
                        (cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y]
                                .getColor() - playerColor);
                if (cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y].getColor() < 0)
                    cells[tail[playerColor - 1].getIntPair().x][tail[playerColor - 1].getIntPair().y].setColor(0);
                tail[playerColor - 1] = tail[playerColor - 1].getNext();
            } else {
                counter[playerColor - 1]--;
                length[playerColor - 1]++;
            }
            cellValues[cellNum.x][cellNum.y] = 0;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cellValues[i][j] == 0) {
                    int n = ThreadLocalRandom.current().nextInt(0, 10000);
                    if (n <= 9995) {
                        cellValues[i][j] = 0;
                    } else if (n <= 9998) {
                        cellValues[i][j] = 1;
                    } else {
                        cellValues[i][j] = 2;
                    }
                }
            }
        }
        if (numberOfMoves > maxNumberOfMoves) {
            return -2;
        }
        return 0;
    }

    private boolean adj(IntPair intPair1, IntPair intPair2) {
        return (intPair1.x == intPair2.x && Math.abs(intPair1.y - intPair2.y) == 1) ||
                (intPair1.y == intPair2.y && Math.abs(intPair1.x - intPair2.x) == 1);
    }

    public int getCellValues(int i, int j) {
        return cellValues[i][j];
    }
}