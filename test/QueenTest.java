package test;

import static org.junit.jupiter.api.Assertions.*;

import model.Board;
import model.Piece;
import model.Queen;
import org.junit.Test;

import java.util.List;

public class QueenTest {

    @Test
    public void testGetPotentialMoves_EmptyBoard_CenterPosition() {
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen("White", 4, 4);
        board[4][4] = queen;

        List<Board.Move> moves = queen.getPotentialMoves(4, 4, board);

        assertEquals(27, moves.size(), "The queen should have 27 possible moves from the center on an empty board.");
    }

    @Test
    public void testGetPotentialMoves_CaptureOpponentPiece() {
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen("White", 3, 3);
        board[3][3] = queen;
        Piece opponentPiece = new Queen("Black", 5, 5);
        board[5][5] = opponentPiece;

        List<Board.Move> moves = queen.getPotentialMoves(3, 3, board);

        assertTrue(moves.stream().anyMatch(move -> move.toX == 5 && move.toY == 5),
                "The queen should be able to capture the opponent's piece.");
    }

    @Test
    public void testGetPotentialMoves_BlockedBySameColorPiece() {
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen("White", 3, 3);
        board[3][3] = queen;
        Piece sameColorPiece = new Queen("White", 3, 5);
        board[3][5] = sameColorPiece;

        List<Board.Move> moves = queen.getPotentialMoves(3, 3, board);

        assertFalse(moves.stream().anyMatch(move -> move.toX == 3 && move.toY == 5),
                "The queen should not move to a square occupied by a piece of the same color.");
    }

    @Test
    public void testGetPotentialMoves_EmptyBoard_CornerPosition() {
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen("White", 0, 0);
        board[0][0] = queen;

        List<Board.Move> moves = queen.getPotentialMoves(0, 0, board);

        assertEquals(21, moves.size(), "The queen should have 21 possible moves from the corner on an empty board.");
    }

    @Test
    public void testGetPotentialMoves_BlockedInAllDirections() {
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen("White", 4, 4);
        board[4][4] = queen;
        board[3][4] = new Queen("White", 3, 4); // Block north
        board[5][4] = new Queen("White", 5, 4); // Block south
        board[4][3] = new Queen("White", 4, 3); // Block west
        board[4][5] = new Queen("White", 4, 5); // Block east
        board[3][3] = new Queen("White", 3, 3); // Block northwest
        board[3][5] = new Queen("White", 3, 5); // Block northeast
        board[5][3] = new Queen("White", 5, 3); // Block southwest
        board[5][5] = new Queen("White", 5, 5); // Block southeast

        List<Board.Move> moves = queen.getPotentialMoves(4, 4, board);

        assertEquals(0, moves.size(), "The queen should have no possible moves when surrounded by pieces of the same color.");
    }
}
