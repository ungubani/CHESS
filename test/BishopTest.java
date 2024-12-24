package test;

import model.*;

import java.util.List;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BishopTest {

    @Test
    public void testIsPathClear_NoObstacles() {
        Board board = new Board();
        Bishop bishop = new Bishop("white", 2, 2);
        board.setPieceAt(2, 2, bishop);

        boolean result = bishop.isPathClear(board, 2, 2, 4, 4);

        assertTrue(result, "Expected the path to be clear as there are no obstacles.");
    }

    @Test
    public void testIsPathClear_ObstacleInMiddle() {
        Board board = new Board();
        Bishop bishop = new Bishop("white", 2, 2);
        board.setPieceAt(2, 2, bishop);

        Piece obstacle = new Pawn("black", 3, 3);
        board.setPieceAt(3, 3, obstacle); // Препятствие

        boolean result = bishop.isPathClear(board, 2, 2, 4, 4);

        assertFalse(result, "Expected the path to be blocked as there is an obstacle at (3, 3).");
    }

    @Test
    public void testIsPathClear_DestinationOccupied() {
        Board board = new Board();
        Bishop bishop = new Bishop("white", 2, 2);
        board.setPieceAt(2, 2, bishop);

        Piece enemy = new Pawn("black", 4, 4);
        board.setPieceAt(4, 4, enemy);

        boolean result = bishop.isPathClear(board, 2, 2, 4, 4);

        assertTrue(result, "Expected the path to be clear since the obstacle is at the destination.");
    }

    @Test
    public void testIsPathClear_InvalidMove_NotDiagonal() {
        Board board = new Board();
        Bishop bishop = new Bishop("white", 2, 2);
        board.setPieceAt(2, 2, bishop);

        boolean result = bishop.isPathClear(board, 2, 2, 2, 4);

        assertFalse(result, "Expected the path to be blocked as it's not a valid diagonal move for a bishop.");
    }
}
