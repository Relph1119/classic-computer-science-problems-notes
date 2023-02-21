package com.teapot.chapter8.common;

/**
 * P156
 * 棋子接口
 */
public interface Piece {
    /**
     * 回合指示器，用于明确在给定的回合后，轮到谁来走棋
     */
    Piece opposite();
}
