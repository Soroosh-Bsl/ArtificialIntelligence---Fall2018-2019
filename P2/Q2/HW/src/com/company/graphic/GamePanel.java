package com.company.graphic;

import com.company.Board;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public final static int size = 30;
    private final static int dia = 25;
    private final static int d = 20, d1 = 10;
    private Board board;
    private Color[] colors = {Color.GRAY, new Color(0, 142, 238), new Color(238, 0, 31),
            new Color(195, 41, 234)};

    public GamePanel(Board board) {
        this.board = board;
        setLayout(null);
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(size * (Board.size + 1), size * (Board.size + 1)));
        setSize(size * (Board.size + 1), size * (Board.size + 1));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < Board.size; i++)
            for (int j = 0; j < Board.size; j++) {

                if (board.getCell(Board.size - 1 - j, i).getColor() == 0) {
                    drawCenteredRectangle((Graphics2D) g, size * (i + 1), size * (j + 1),
                            colors[board.getCell(Board.size - 1 - j, i).getColor()], "");
                    if (board.getCellValues(Board.size - 1 - j, i) == 1) {
                        drawOval((Graphics2D) g, size * (i + 1), size * (j + 1), 1);
                    }
                    if (board.getCellValues(Board.size - 1 - j, i) == 2) {
                        drawOval((Graphics2D) g, size * (i + 1), size * (j + 1), 2);
                    }
                } else if (board.getHead(1).x == Board.size - 1 - j && board.getHead(1).y == i) {
                    if (board.getCell(Board.size - 1 - j, i).getColor() == 1)
                        drawCenteredRectangle((Graphics2D) g, size * (i + 1), size * (j + 1),
                                colors[0], "");
                    else
                        drawCenteredRectangle((Graphics2D) g, size * (i + 1), size * (j + 1),
                                colors[2], "fill");
                    drawCenteredCircle((Graphics2D) g, size * (i + 1), size * (j + 1), colors[1]);
                } else if (board.getHead(2).x == Board.size - 1 - j && board.getHead(2).y == i) {
                    if (board.getCell(Board.size - 1 - j, i).getColor() == 2)
                        drawCenteredRectangle((Graphics2D) g, size * (i + 1), size * (j + 1),
                                colors[0], "");
                    else
                        drawCenteredRectangle((Graphics2D) g, size * (i + 1), size * (j + 1),
                                colors[1], "fill");
                    drawCenteredCircle((Graphics2D) g, size * (i + 1), size * (j + 1), colors[2]);
                } else
                    drawCenteredRectangle((Graphics2D) g, size * (i + 1), size * (j + 1),
                            colors[board.getCell(Board.size - 1 - j, i).getColor()], "fill");
            }
    }

    private void drawCenteredCircle(Graphics2D g, int x, int y, Color col) {
        g.setColor(col);
        x = x - (d / 2);
        y = y - (d / 2);
        g.fillOval(x, y, d, d);
    }

    private void drawOval(Graphics2D g, int x, int y, int isFill) {
        g.setColor(Color.ORANGE);
        x = x - (d1 / 2);
        y = y - (d1 / 2);
        if (isFill == 1) {
            g.drawOval(x, y, d1, d1);
        } else {
            g.fillOval(x, y, d1, d1);
        }
    }

    private void drawCenteredRectangle(Graphics2D g, int x, int y, Color col, String str) {
        g.setColor(col);
        g.drawString(str, x, y);
        x = x - (GamePanel.dia / 2);
        y = y - (GamePanel.dia / 2);
        if (str.equals("fill")) {
            g.fillRect(x, y, GamePanel.dia, GamePanel.dia);
        } else
            g.drawRect(x, y, GamePanel.dia, GamePanel.dia);
    }
}