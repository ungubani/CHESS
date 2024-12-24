package test;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import model.Board;
import model.Piece;
import model.Rook;

public class RookTest {

    /**
     * Tests for the `canAttack` method in the `Rook` class.
     * <p>
     * The `canAttack` method checks if the rook can attack a specific square on the board,
     * taking into account whether the path is clear or blocked.
     */


    @Test
    public void testCanAttackHorizontalPathClear() {
        // Arrange
        Board board = new Board();
        Rook rook = new Rook("white", 4, 4);

        // Act & Assert
        assertTrue(rook.canAttack(4, 6, board), "Rook should attack horizontally to (4,6)");
    }

    @Test
    public void testCanAttackVerticalPathClear() {
        // Arrange
        Board board = new Board();
        Rook rook = new Rook("white", 4, 4);

        // Act & Assert
        assertTrue(rook.canAttack(1, 4, board), "Rook should attack vertically to (1,4)");
    }

    @Test
    public void testCannotAttackDiagonal() {
        // Arrange
        Board board = new Board(true);
        Rook rook = new Rook("white", 4, 4);

        board.setPieceAt(4, 4, rook);

        // Act & Assert
        assertFalse(rook.canAttack(5, 5, board), "Rook cannot attack diagonally to (5,5)");
    }

    @Test
    public void testCannotAttackBlockedPath() {

        Board board = new Board();
        Rook rook = new Rook("white", 4, 4);
        board.setPieceAt(4, 4, rook);

        Rook blockingPiece = new Rook("white", 4, 5);
        board.setPieceAt(4, 5, blockingPiece);

        assertFalse(rook.canAttack(4, 6, board), "Rook cannot attack horizontally to (4,6) because the path is blocked");
    }
}
