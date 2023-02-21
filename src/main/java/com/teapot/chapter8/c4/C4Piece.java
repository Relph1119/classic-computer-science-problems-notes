package com.teapot.chapter8.c4;

import com.teapot.chapter8.common.Piece;

/**
 * P169
 * 四子棋的棋子
 */
public enum C4Piece implements Piece {
    B, R, E;

    @Override
    public C4Piece opposite() {
        switch (this) {
            case B:
                return C4Piece.R;
            case R:
                return C4Piece.B;
            default:
                return C4Piece.E;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case B:
                return "B";
            case R:
                return "R";
            default:
                return " ";
        }
    }
}
