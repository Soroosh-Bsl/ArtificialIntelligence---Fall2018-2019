package com.company;

import com.company.graphic.GamePanel;
import com.company.players.Player;

import javax.swing.*;
import java.awt.*;

class Game {

    private final static int timeLimit = 500;
    private Player[] players = new Player[2];
    private Board board;
    private int turn = 0;
    private IntPair newHead;

    Game(Player p1, Player p2) {
        players[0] = p1;
        players[1] = p2;
    }

    void start() {
        board = new Board();
        GamePanel gamePanel = new GamePanel(board);
        gamePanel.setBounds(0, 0, GamePanel.size * (Board.size + 1), GamePanel.size * (Board.size + 1));
        JFrame frame = new JFrame("WengerCup2");
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(GamePanel.size * (Board.size + 1), GamePanel.size * (Board.size + 1) + 22));
        frame.pack();
        frame.setVisible(true);
        frame.add(gamePanel);
        gamePanel.repaint();
        while (board.win() == 0) {
            newHead = new IntPair(-10, -10);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread t = new Thread(() -> {
                try {
                    newHead = players[turn].getMove(new Board(board));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
            try {
                t.join(timeLimit);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            if (newHead.x == -10) {
                System.out.println("Player " + players[turn].getCol() + " has exceeded the time limit\n" +
                        "Player " + players[1 - turn].getCol() + " has won\n");
                return;
            }
            int res = board.move(newHead, turn + 1);
            if (res == -1) {
                System.out.println("("+board.getHead(players[turn].getCol()).x+", "+board.getHead(players[turn].getCol()).y+")" + "  , " + "("+newHead.x+" ,"+newHead.y+")");
                System.out.println("Player " + players[turn].getCol() + " has made an invalid move\n" +
                        "Player " + players[1 - turn].getCol() + " has won\n");
                return;
            }
            if (res == -2) {
                System.out.println("No more moves!");
                if (board.getLength(1) > board.getLength(2)) {
                    System.out.println("Player 1 has won");
                    System.out.println("Length player 1: " + board.getLength(1));
                    System.out.println("Length player 2: " + board.getLength(2));
                } else if (board.getLength(1) < board.getLength(2)) {
                    System.out.println("Player 2 has won");
                    System.out.println("Length player 1: " + board.getLength(1));
                    System.out.println("Length player 2: " + board.getLength(2));
                } else {
                    System.out.println("Draw!");
                    System.out.println("Length player 1: " + board.getLength(1));
                    System.out.println("Length player 2: " + board.getLength(2));
                }
                return;
            }
            gamePanel.repaint();
            turn = 1 - turn;
        }
        System.out.println("Player " + board.win() + " has won\n");
    }
}
