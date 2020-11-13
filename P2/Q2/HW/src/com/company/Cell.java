package com.company;


public class Cell {

    private int row, column;
    private int color;

    Cell(int row, int column, int color) {
        this(row, column);
        this.color = color;
    }

    private Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    Cell(Cell cell) {
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.color = cell.getColor();
    }

    private int getRow() {
        return row;
    }

    private int getColumn() {
        return column;
    }

    public int getColor() {
        return color;
    }

    void setColor(int color) {
        this.color = color;
    }
}
