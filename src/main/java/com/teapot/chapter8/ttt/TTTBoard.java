package com.teapot.chapter8.ttt;

import com.teapot.chapter8.common.Board;
import com.teapot.chapter8.common.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * P159~161
 * 井字棋的棋盘，标号遵循从左到右，从上到下为0~8
 */
public class TTTBoard implements Board<Integer> {
    // 井字棋的棋盘有9个位置
    private static final int NUM_SQUARES = 9;
    private final TTTPiece[] position;
    private final TTTPiece turn;

    public TTTBoard(TTTPiece[] position, TTTPiece turn) {
        this.position = position;
        this.turn = turn;
    }

    public TTTBoard() {
        position = new TTTPiece[NUM_SQUARES];
        Arrays.fill(position, TTTPiece.E);
        turn = TTTPiece.X;
    }

    @Override
    public Piece getTurn() {
        return turn;
    }

    @Override
    public TTTBoard move(Integer location) {
        TTTPiece[] tempPosition = Arrays.copyOf(position, position.length);
        tempPosition[location] = turn;
        return new TTTBoard(tempPosition, turn.opposite());
    }

    /**
     * 检查棋盘上所有空着的方格，并以列表的形式返回
     */
    @Override
    public List<Integer> getLegalMoves() {
        ArrayList<Integer> legalMoves = new ArrayList<>();
        for (int i = 0; i < NUM_SQUARES; i++) {
            if (position[i] == TTTPiece.E) {
                legalMoves.add(i);
            }
        }
        return legalMoves;
    }

    @Override
    public boolean isWin() {
        return checkPos(0, 1, 2) || checkPos(3, 4, 5) || checkPos(6, 7, 8) ||
                checkPos(0, 3, 6) || checkPos(1, 4, 7) || checkPos(2, 5, 8) ||
                checkPos(0, 4, 8) || checkPos(2, 4, 6);
    }

    private boolean checkPos(int p0, int p1, int p2) {
        return position[p0] == position[p1] && position[p0] == position[p2] && position[p0] != TTTPiece.E;
    }

    @Override
    public double evaluate(Piece player) {
        if (isWin() && turn == player) {
            return -1;
        } else if (isWin() && turn != player) {
            return 1;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                sb.append(position[row * 3 + col].toString());
                if (col != 2) {
                    sb.append("|");
                }
            }
            sb.append(System.lineSeparator());
            if (row != 2) {
                sb.append("-----");
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
