package test;


import model.Board;
import model.*;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    /**
     * Test class for the `getValidMoves` method in the `Board` class.
     * The `getValidMoves` method computes all valid moves for a piece located
     * at the given coordinates on the chessboard.
     */

    @Test
    public void testGetValidMovesForPawnInitialPosition() {
        Board board = new Board();

        // White pawn at initial position (6,4)
        List<Board.Move> validMoves = board.getValidMoves(6, 4);

        assertEquals(2, validMoves.size(), "A pawn in its initial position can move 1 or 2 squares forward");
        assertTrue(validMoves.contains(new Board.Move(board.getPieceAt(6, 4), null, 6, 4, 5, 4)));
        assertTrue(validMoves.contains(new Board.Move(board.getPieceAt(6, 4), null, 6, 4, 4, 4)));
    }

    @Test
    public void testGetValidMovesForBlockedPawn() {
        Board board = new Board();

        // Block white pawn at (6,4) with another piece
        board.applyMove(new Board.Move(board.getPieceAt(1, 4), null, 1, 4, 5, 4)); // Move black pawn to block

        List<Board.Move> validMoves = board.getValidMoves(6, 4);

        assertEquals(0, validMoves.size(), "A pawn cannot move forward if blocked by another piece");
    }

    @Test
    public void testGetValidMovesForRookUnblocked() {
        Board board = new Board(true);

        Rook rook = new Rook("white", 7, 0);
        board.setPieceAt(5, 0, rook);

        List<Board.Move> validMoves = board.getValidMoves(5, 0);

        // Check rook has valid horizontal and vertical moves
        assertTrue(validMoves.stream().anyMatch(move -> move.toX == 4 && move.toY == 0));
        assertTrue(validMoves.stream().anyMatch(move -> move.toX == 5 && move.toY == 1));
    }

    @Test
    public void testGetValidMovesForKingInCheck() {
        Board board = new Board(true);

        // Устанавливаем короля и королеву на доску
        King king = new King("black", 2, 4);
        board.setPieceAt(2, 4, king);

        Queen queen = new Queen("white", 2, 3); // Белая королева угрожает королю
        board.setPieceAt(2, 3, queen);

        List<Board.Move> validMoves = board.getValidMoves(2, 4);

        // У короля не должно быть валидных ходов, так как любая клетка под атакой
        assertTrue(validMoves.isEmpty(), "The king cannot move into squares attacked by an enemy piece");
    }

    @Test
    public void testGetValidMovesForKnight() {
        Board board = new Board(true);

        // Устанавливаем белого коня на позицию (5, 2)
        Knight knight = new Knight("white", 5, 2);
        board.setPieceAt(5, 2, knight);

        List<Board.Move> validMoves = board.getValidMoves(5, 2);

        // Проверяем количество возможных ходов (8 в открытой позиции)
        assertEquals(8, validMoves.size(), "A knight should have 8 possible moves in an open position");

        // Проверяем некоторые конкретные допустимые ходы
        assertTrue(validMoves.stream().anyMatch(move -> move.toX == 3 && move.toY == 3));
        assertTrue(validMoves.stream().anyMatch(move -> move.toX == 4 && move.toY == 0));
    }

    @Test
    public void testGetValidMovesForEmptySquare() {
        Board board = new Board();

        // Test an empty square
        List<Board.Move> validMoves = board.getValidMoves(4, 4);

        assertEquals(0, validMoves.size(), "There should be no valid moves for an empty square");
    }

    @Test
    public void testGetValidMovesForOpponentPiece() {
        Board board = new Board();

        // Test valid moves of a black piece when white is the current player
        List<Board.Move> validMoves = board.getValidMoves(1, 0);

        assertEquals(0, validMoves.size(), "Opponent's pieces should have no valid moves on the current turn");
    }
}