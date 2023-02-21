package com.teapot.chapter8.ttt;

import com.teapot.chapter8.common.Piece;

/**
 * P158
 * 井字棋
 */
public enum TTTPiece implements Piece {
    // E表示空
    X, O, E;

    @Override
    public TTTPiece opposite() {
        // 交换下棋顺序
        switch (this) {
            case X:
                return TTTPiece.O;
            case O:
                return TTTPiece.X;
            default:
                return TTTPiece.E;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case X:
                return "X";
            case O:
                return "O";
            default:
                return " ";
        }
    }
}
