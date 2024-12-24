package test;

import model.Board;
import model.King;
import model.Piece;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {

    /**
     * This class tests the `canAttack` method of the `King` class.
     * <p>
     * The `canAttack` method determines whether the King can attack
     * a specific square on the chessboard based on its movement rules.
     */

    @Test
    public void testCanAttackAdjacentSquareOccupied() {
        // Arrange
        Board board = new Board(true);
        King king = new King("white", 4, 4);

        // Act
        boolean result = king.canAttack(5, 5, board);

        // Assert
        assertTrue(result, "King should be able to attack (5, 5) from (4, 4)");
    }

    @Test
    public void testCanAttackAdjacentSquareEmpty() {
        // Arrange
        Board board = new Board();
        King king = new King("black", 1, 1);

        // Act
        boolean result = king.canAttack(1, 2, board);

        // Assert
        assertTrue(result, "King should be able to attack (1, 2) from (1, 1)");
    }

    @Test
    public void testCannotAttackDistantSquare() {
        // Arrange
        Board board = new Board();
        King king = new King("white", 7, 7);

        // Act
        boolean result = king.canAttack(5, 5, board);

        // Assert
        assertFalse(result, "King should not be able to attack (5, 5) from (7, 7)");
    }

    @Test
    public void testCannotAttackSameSquare() {
        // Arrange
        Board board = new Board();
        King king = new King("black", 3, 3);

        // Act
        boolean result = king.canAttack(3, 3, board);

        // Assert
        assertFalse(result, "King should not be able to attack the same square it is on");
    }

    @Test
    public void testCanAttackEdgeCase() {
        // Arrange
        Board board = new Board();
        King king = new King("white", 0, 0);

        // Act
        boolean result = king.canAttack(1, 1, board);

        // Assert
        assertTrue(result, "King should be able to attack (1, 1) from (0, 0)");
    }

    @Test
    public void testCannotAttackOutOfBounds() {
        // Arrange
        Board board = new Board();
        King king = new King("black", 0, 0);

        // Act
        boolean result = king.canAttack(-1, -1, board);

        // Assert
        assertFalse(result, "King should not be able to attack a square out of bounds (-1, -1)");
    }
}