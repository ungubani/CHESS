package model;

import model.Board.Move;

import java.util.List;

public class ChessAI {
    private final Board board;
    private final int depth = 4;

    public ChessAI(Board board) {
        this.board = board;
    }

    public Move calculateBestMove(int depth) {
        List<Move> validMoves = board.getAllValidMoves("black");
        if (validMoves.isEmpty()) {
            return null;
        }

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (Move move : validMoves) {
            board.applyMove(move);

            int score = minimax(depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

            board.undoMove();

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(int depth, boolean maximizingPlayer, int alpha, int beta) {
        if (depth == 0 || board.checkGameEnd() != 0) {
            return evaluateBoard();
        }

        List<Move> validMoves = maximizingPlayer
                ? board.getAllValidMoves("black")
                : board.getAllValidMoves("white");

        if (validMoves.isEmpty()) {
            return evaluateBoard();
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (Move move : validMoves) {
                board.applyMove(move);
                int eval = minimax(depth - 1, false, alpha, beta);
                board.undoMove();
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }

            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;

            for (Move move : validMoves) {
                board.applyMove(move);
                int eval = minimax(depth - 1, true, alpha, beta);
                board.undoMove();
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }

            return minEval;
        }
    }

    public int evaluateBoard() {
        if (board.checkGameEnd() == -2) {
            return Integer.MAX_VALUE;
        }
        if (board.checkGameEnd() == 2) {
            return Integer.MIN_VALUE;
        }
        if (Math.abs(board.checkGameEnd()) == 1) {
            return 0;
        }

        int score = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPieceAt(x, y);
                if (piece != null) {
                    if (piece.getColor().equals("black")) {
                        score += getPieceValue(piece);
                    } else if (piece.getColor().equals("white")) {
                        score -= getPieceValue(piece);
                    }
                }
            }
        }
        return score;
    }

    private int getPieceValue(Piece piece) {
        if (piece instanceof Pawn) {
            return 1;
        } else if (piece instanceof Knight) {
            return 3;
        } else if (piece instanceof Bishop) {
            return 3;
        } else if (piece instanceof Rook) {
            return 5;
        } else if (piece instanceof Queen) {
            return 9;
        } else if (piece instanceof King) {
            return 1000;
        }
        return 0;
    }

    public boolean performMove() {
        Move bestMove = calculateBestMove(depth);
        if (bestMove != null) {
            return board.movePiece(bestMove.fromX, bestMove.fromY, bestMove.toX, bestMove.toY);
        }
        return false;
    }
}