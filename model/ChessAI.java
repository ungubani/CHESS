package model;

import model.Board.Move;

public class ChessAI {
    public Move calculateNextMove(Board board, String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPieceAt(i, j);
                if (piece != null && piece.getColor().equals(color)) {
                    for (int toX = 0; toX < 8; toX++) {
                        for (int toY = 0; toY < 8; toY++) {
                            if (piece.isValidMove(toX, toY, board)) {
                                return new Move(piece, board.getPieceAt(toX, toY), i, j, toX, toY);
                            }
                        }
                    }
                }
            }
        }
        return null; // Нет доступных ходов
    }
}

