package test;

import model.Board;
import model.Knight;
import model.Piece;
import model.Board.Move;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KnightTest {

    /**
     * Tests for the Knight class's getPotentialMoves method.
     * The method calculates all valid moves for a Knight piece on a chessboard.
     */

    @Test
    public void testKnightMovesFromCenter() {
        // Arrange
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight("White", 4, 4);
        board[4][4] = knight;

        // Act
        List<Move> moves = knight.getPotentialMoves(4, 4, board);

        // Assert
        assertEquals(8, moves.size(), "A knight in the center should have 8 possible moves.");
        assertTrue(moves.contains(new Move(knight, null, 4, 4, 2, 3)));
        assertTrue(moves.contains(new Move(knight, null, 4, 4, 2, 5)));
        assertTrue(moves.contains(new Move(knight, null, 4, 4, 3, 2)));
        assertTrue(moves.contains(new Move(knight, null, 4, 4, 3, 6)));
        assertTrue(moves.contains(new Move(knight, null, 4, 4, 5, 2)));
        assertTrue(moves.contains(new Move(knight, null, 4, 4, 5, 6)));
        assertTrue(moves.contains(new Move(knight, null, 4, 4, 6, 3)));
        assertTrue(moves.contains(new Move(knight, null, 4, 4, 6, 5)));
    }

    @Test
    public void testKnightMovesFromCorner() {
        // Arrange
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight("Black", 0, 0);
        board[0][0] = knight;

        // Act
        List<Move> moves = knight.getPotentialMoves(0, 0, board);

        // Assert
        assertEquals(2, moves.size(), "A knight in the corner should have 2 possible moves.");
        assertTrue(moves.contains(new Move(knight, null, 0, 0, 2, 1)));
        assertTrue(moves.contains(new Move(knight, null, 0, 0, 1, 2)));
    }

    @Test
    public void testKnightMovesWithAlliesBlocking() {
        // Arrange
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight("White", 3, 3);
        board[3][3] = knight;
        board[1][2] = new Knight("White", 1, 2); // Ally piece
        board[2][1] = new Knight("White", 2, 1); // Ally piece

        // Act
        List<Move> moves = knight.getPotentialMoves(3, 3, board);

        // Assert
        assertEquals(6, moves.size(), "Knight with some ally pieces blocking its moves.");
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 1, 4)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 2, 5)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 4, 1)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 4, 5)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 5, 2)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 5, 4)));
    }

    @Test
    public void testKnightMovesWithEnemyPieces() {
        // Arrange
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight("Black", 3, 3);
        board[3][3] = knight;
        board[1][4] = new Knight("White", 1, 4); // Enemy piece
        board[2][5] = new Knight("White", 2, 5); // Enemy piece

        // Act
        List<Move> moves = knight.getPotentialMoves(3, 3, board);

        // Assert
        assertEquals(8, moves.size(), "Knight should be able to capture enemy pieces.");
        assertTrue(moves.contains(new Move(knight, board[1][4], 3, 3, 1, 4)));
        assertTrue(moves.contains(new Move(knight, board[2][5], 3, 3, 2, 5)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 1, 2)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 2, 1)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 4, 1)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 4, 5)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 5, 2)));
        assertTrue(moves.contains(new Move(knight, null, 3, 3, 5, 4)));
    }

    @Test
    public void testNoMovesWhenSurroundedByAllies() {
        // Arrange
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight("White", 4, 4);
        board[4][4] = knight;
        board[2][3] = new Knight("White", 2, 3); // Ally piece
        board[2][5] = new Knight("White", 2, 5); // Ally piece
        board[3][2] = new Knight("White", 3, 2); // Ally piece
        board[3][6] = new Knight("White", 3, 6); // Ally piece
        board[5][2] = new Knight("White", 5, 2); // Ally piece
        board[5][6] = new Knight("White", 5, 6); // Ally piece
        board[6][3] = new Knight("White", 6, 3); // Ally piece
        board[6][5] = new Knight("White", 6, 5); // Ally piece

        // Act
        List<Move> moves = knight.getPotentialMoves(4, 4, board);

        // Assert
        assertEquals(0, moves.size(), "Knight should have no moves when surrounded by allies.");
    }
}