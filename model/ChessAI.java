package model;

import model.Board.Move;

import java.util.List;
import java.util.Random;

public class ChessAI {
    private final Board board;

    public ChessAI(Board board) {
        this.board = board; // Получаем ссылку на текущую игровую доску
    }

    /**
     * Определяет и выполняет лучший ход для компьютера.
     */
    public Move calculateBestMove() {
        // Получаем все доступные ходы для чёрных (или любого цвета компьютера)
        List<Move> validMoves = board.getAllValidMoves("black");
        if (validMoves.isEmpty()) {
            return null; // Нет доступных ходов
        }

        // Простая стратегия: случайный выбор хода
        Random random = new Random();
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    /**
     * Выполняет ход компьютера на доске.
     * @return true, если ход был успешно выполнен.
     */
    public boolean performMove() {
        Move bestMove = calculateBestMove();
        if (bestMove != null) {
            return board.movePiece(bestMove.fromX, bestMove.fromY, bestMove.toX, bestMove.toY);
        }
        return false; // Если хода нет, например, шахматный мат
    }
}

