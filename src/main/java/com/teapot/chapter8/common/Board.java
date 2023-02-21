package com.teapot.chapter8.common;

import java.util.List;

/**
 * P157
 * 位置状态的接口
 */
public interface Board<Move> {
    /**
     * 轮到谁走棋？
     */
    Piece getTurn();

    /**
     * 从当前位置移动到指定的位置
     */
    Board<Move> move(Move location);

    /**
     * 在当前位置有哪些符合规则的走法
     */
    List<Move> getLegalMoves();

    boolean isWin();

    /**
     * 是否平局
     */
    default boolean isDraw() {
        return !isWin() && getLegalMoves().isEmpty();
    }

    /**
     * 评估当前位置，看哪位玩家占据了优势
     */
    double evaluate(Piece player);
}
