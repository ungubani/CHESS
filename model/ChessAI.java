package model;

import model.Board.Move;

import java.util.List;

public class ChessAI {
    private final Board board;
    private final int depth = 4;

    public ChessAI(Board board) {
        this.board = board; // Получаем ссылку на текущую игровую доску
    }

    public Move calculateBestMove(int depth) {
        List<Move> validMoves = board.getAllValidMoves("black"); // Ходы для ИИ
        if (validMoves.isEmpty()) {
            return null; // Нет доступных ходов
        }

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE; // Худшая возможная оценка

        for (Move move : validMoves) {
            // Выполняем пробный ход
            board.applyMove(move);

            // Рассчитываем "лучший" минимакс-ответ с глубиной depth - 1
            int score = minimax(depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

            // Отменяем пробный ход
            board.undoMove();

            // Если этот ход лучше текущего, обновляем результат
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(int depth, boolean maximizingPlayer, int alpha, int beta) {
        // Основные условия выхода
        if (depth == 0 || board.checkGameEnd() != 0) {
            return evaluateBoard(); // Оценка текущей позиции
        }

        List<Move> validMoves = maximizingPlayer
                ? board.getAllValidMoves("black") // Ходы ИИ (maximizing)
                : board.getAllValidMoves("white"); // Ходы игрока (minimizing)

        if (validMoves.isEmpty()) {
            return evaluateBoard(); // Позиция остаётся неизменной, если нет ходов
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (Move move : validMoves) {
                board.applyMove(move); // Пробный ход
                int eval = minimax(depth - 1, false, alpha, beta); // Углубляемся за противника
                board.undoMove(); // Откат хода
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break; // Альфа-бета отсечение
            }

            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;

            for (Move move : validMoves) {
                board.applyMove(move); // Пробный ход
                int eval = minimax(depth - 1, true, alpha, beta); // Углубляемся за себя
                board.undoMove(); // Откат хода
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break; // Альфа-бета отсечение
            }

            return minEval;
        }
    }

    public int evaluateBoard() {
        if (board.checkGameEnd() == -2) {
            return Integer.MAX_VALUE; // Чёрные ставят мат
        }
        if (board.checkGameEnd() == 2) {
            return Integer.MIN_VALUE; // Белые ставят мат
        }
        if (Math.abs(board.checkGameEnd()) == 1) {
            return 0; // Пат
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
            return 1; // Пешка
        } else if (piece instanceof Knight) {
            return 3; // Конь
        } else if (piece instanceof Bishop) {
            return 3; // Слон
        } else if (piece instanceof Rook) {
            return 5; // Ладья
        } else if (piece instanceof Queen) {
            return 9; // Ферзь
        } else if (piece instanceof King) {
            return 1000; // Король
        }
        return 0; // Если фигура неизвестна (на всякий случай)
    }

    /**
     * Выполняет ход компьютера на доске.
     * @return true, если ход был успешно выполнен.
     */
    public boolean performMove() {

        Move bestMove = calculateBestMove(depth);
        if (bestMove != null) {
            return board.movePiece(bestMove.fromX, bestMove.fromY, bestMove.toX, bestMove.toY);
        }
        return false; // Если хода нет, например, шахматный мат
    }
}

