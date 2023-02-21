package com.teapot.chapter8.common;

/**
 * P163
 * 极小化极大算法
 */
public class Minimax {
    public static <MOVE> double minimax(Board<MOVE> board, boolean maximizing, Piece originalPlayer, int maxDepth) {
        if (board.isWin() || board.isDraw() || maxDepth == 0) {
            return board.evaluate(originalPlayer);
        }

        if (maximizing) {
            // 求最大值，查找能够产生最高评分的走法
            double bestEval = Double.NEGATIVE_INFINITY;
            for (MOVE move: board.getLegalMoves()) {
                double result = minimax(board.move(move), false, originalPlayer, maxDepth - 1);
                bestEval = Math.max(result, bestEval);
            }
            return bestEval;
        } else {
            // 求最小值，查找能够产生最低评分的走法
            double worstEval = Double.POSITIVE_INFINITY;
            for (MOVE move: board.getLegalMoves()) {
                double result = minimax(board.move(move), true, originalPlayer, maxDepth - 1);
                worstEval = Math.min(result, worstEval);
            }
            return worstEval;
        }
    }

    public static <Move> Move findBestMove(Board<Move> board, int maxDepth) {
        double bestEval = Double.NEGATIVE_INFINITY;
        Move bestMove = null;
        for (Move move: board.getLegalMoves()) {
//            double result = minimax(board.move(move), false, board.getTurn(), maxDepth);
            double result = alphabeta(board.move(move), false, board.getTurn(), maxDepth);
            if (result > bestEval) {
                bestEval = result;
                bestMove = move;
            }
        }
        return bestMove;
    }

    public static <Move> double alphabeta(Board<Move> board, boolean maximizing, Piece originalPlayer, int maxDepth) {
        return alphabeta(board, maximizing, originalPlayer, maxDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * alpha-beta剪枝算法
     *
     * @param alpha 表示搜索树当前找到的最优极大化走法的评分
     * @param beta 表示当前找到的对手的最优极小化走法的评分
     */
    private static <Move> double alphabeta(Board<Move> board, boolean maximizing, Piece originalPlayer,
                                           int maxDepth, double alpha, double beta) {
        if (board.isWin() || board.isDraw() || maxDepth == 0) {
            return board.evaluate(originalPlayer);
        }

        if (maximizing) {
            for (Move m: board.getLegalMoves()) {
                alpha = Math.max(alpha, alphabeta(board.move(m), false, originalPlayer, maxDepth - 1, alpha, beta));
                // 不值得对该搜索分支更进一步的搜索
                if (beta <= alpha) {
                    break;
                }
            }
            return alpha;
        } else {
            for (Move m: board.getLegalMoves()) {
                beta = Math.min(beta, alphabeta(board.move(m), true, originalPlayer, maxDepth - 1, alpha, beta));
                if (beta <= alpha) {
                    break;
                }
            }
            return beta;
        }
    }
}
