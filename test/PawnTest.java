package test;

import model.Board;
import model.Pawn;
import model.Piece;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {

    /**
     * Test class for the {@link Pawn#getPotentialMoves(int, int, Piece[][])} method.
     * This method calculates all possible moves for a pawn from its current position on the board.
     */

//    @Test
//    public void testPawnMoveForwardOneStep() {
//        // Setup
//        Pawn pawn = new Pawn("white", 6, 4);
//        Piece[][] board = new Piece[8][8];
//
//        // Test
//        List<Board.Move> moves = pawn.getPotentialMoves(6, 4, board);
//
//        // Verify
//        assertEquals(1, moves.size());
//        Board.Move expectedMove = new Board.Move(pawn, null, 6, 4, 5, 4);
//        assertEquals(expectedMove, moves.get(0));
//    }

//    @Test
//    public void testPawnMoveForwardOneStep() {
//        // Setup
//        Board board = new Board(true); // Создаем чистую доску
//        Pawn pawn = new Pawn("white", 6, 4);
//        board.setPieceAt(6, 4, pawn); // Размещаем белую пешку на позиции (6, 4)
//
//        // Test
//        List<Board.Move> moves = board.getValidMoves(6, 4); // Получаем доступные ходы для пешки
//
//        // Verify
//        assertEquals(1, moves.size(), "A pawn should be able to move one step forward");
//        Board.Move expectedMove = new Board.Move(pawn, null, 6, 4, 5, 4);
//        assertTrue(moves.contains(expectedMove), "Pawn should move one step forward to (5, 4)");
//    }

    @Test
    public void testPawnMoveForwardOneStep() {
        // Setup
        Board board = new Board(true); // Создаем чистую доску
        Pawn pawn = new Pawn("white", 6, 4);
        board.setPieceAt(6, 4, pawn); // Помещаем белую пешку в начальную позицию (6, 4)

        // Test
        List<Board.Move> moves = board.getValidMoves(6, 4); // Получаем доступные ходы для пешки

        // Ожидаемый ход пешки вперед
        Board.Move expectedMove = new Board.Move(pawn, null, 6, 4, 5, 4);

        // Проверка с использованием anyMatch()
        assertTrue(moves.stream().anyMatch(move -> move.equals(expectedMove)), "Pawn should move one step forward to (5, 4)");
    }

    @Test
    public void testPawnMoveForwardTwoStepsFromStartingPosition() {
        // Setup
        Pawn pawn = new Pawn("white", 6, 4);
        Piece[][] board = new Piece[8][8];

        // Test
        List<Board.Move> moves = pawn.getPotentialMoves(6, 4, board);

        // Verify
        assertEquals(2, moves.size());
        Board.Move expectedMoveOne = new Board.Move(pawn, null, 6, 4, 5, 4);
        Board.Move expectedMoveTwo = new Board.Move(pawn, null, 6, 4, 4, 4);
        assertEquals(expectedMoveOne, moves.get(0));
        assertEquals(expectedMoveTwo, moves.get(1));
    }

    @Test
    public void testPawnCannotMoveForwardWhenBlocked() {
        // Setup
        Pawn pawn = new Pawn("white", 6, 4);
        Piece[][] board = new Piece[8][8];
        board[5][4] = new Pawn("black", 5, 4);

        // Test
        List<Board.Move> moves = pawn.getPotentialMoves(6, 4, board);

        // Verify
        assertEquals(0, moves.size());
    }

//    @Test
//    public void testPawnMoveCaptureDiagonally() {
//        // Setup
//        Pawn pawn = new Pawn("white", 6, 4);
//        Piece[][] board = new Piece[8][8];
//        board[5][3] = new Pawn("black", 5, 3);
//        board[5][5] = new Pawn("black", 5, 5);
//
//        // Test
//        List<Board.Move> moves = pawn.getPotentialMoves(6, 4, board);
//
//        // Verify
//        assertEquals(3, moves.size());
//        Board.Move expectedForwardMove = new Board.Move(pawn, null, 6, 4, 5, 4);
//        Board.Move expectedLeftCapture = new Board.Move(pawn, board[5][3], 6, 4, 5, 3);
//        Board.Move expectedRightCapture = new Board.Move(pawn, board[5][5], 6, 4, 5, 5);
//        assertEquals(expectedForwardMove, moves.get(0));
//        assertEquals(expectedLeftCapture, moves.get(1));
//        assertEquals(expectedRightCapture, moves.get(2));
//    }

    @Test
    public void testPawnMoveCaptureDiagonally() {
        // Setup
        Board board = new Board(true); // Создаем пустую доску
        Pawn pawn = new Pawn("white", 6, 4);
        board.setPieceAt(6, 4, pawn); // Помещаем белую пешку в начальную позицию (6, 4)

        // Размещаем черные пешки на диагоналях для проверки захвата
        Pawn blackPawnLeft = new Pawn("black", 5, 3);
        Pawn blackPawnRight = new Pawn("black", 5, 5);
        board.setPieceAt(5, 3, blackPawnLeft);
        board.setPieceAt(5, 5, blackPawnRight);

        // Test
        List<Board.Move> moves = board.getValidMoves(6, 4); // Получаем доступные ходы для белой пешки

        // Ожидаемые ходы
        Board.Move expectedForwardMove = new Board.Move(pawn, null, 6, 4, 5, 4); // Ход вперед
        Board.Move expectedLeftCapture = new Board.Move(pawn, blackPawnLeft, 6, 4, 5, 3); // Захват налево
        Board.Move expectedRightCapture = new Board.Move(pawn, blackPawnRight, 6, 4, 5, 5); // Захват направо

        // Проверяем с использованием anyMatch, что все ожидаемые ходы присутствуют в списке moves
        assertTrue(moves.stream().anyMatch(move -> move.equals(expectedForwardMove)),
                "Pawn should move one step forward to (5, 4)");
        assertTrue(moves.stream().anyMatch(move -> move.equals(expectedLeftCapture)),
                "Pawn should capture diagonally on (5, 3)");
        assertTrue(moves.stream().anyMatch(move -> move.equals(expectedRightCapture)),
                "Pawn should capture diagonally on (5, 5)");
    }

//    @Test
//    public void testPawnCannotCaptureDiagonallySameColor() {
//        // Setup
//        Pawn pawn = new Pawn("white", 6, 4);
//        Piece[][] board = new Piece[8][8];
//        board[5][3] = new Pawn("white", 5, 3);
//        board[5][5] = new Pawn("white", 5, 5);
//
//        // Test
//        List<Board.Move> moves = pawn.getPotentialMoves(6, 4, board);
//
//        // Verify
//        assertEquals(1, moves.size());
//        Board.Move expectedForwardMove = new Board.Move(pawn, null, 6, 4, 5, 4);
//        assertEquals(expectedForwardMove, moves.get(0));
//    }

    @Test
    public void testPawnCannotCaptureDiagonallySameColor() {
        // Setup
        Board board = new Board(true); // Создаем пустую доску
        Pawn pawn = new Pawn("white", 6, 4);
        board.setPieceAt(6, 4, pawn); // Помещаем белую пешку в начальную позицию (6, 4)

        // Размещаем белые пешки на диагоналях
        Pawn whitePawnLeft = new Pawn("white", 5, 3);
        Pawn whitePawnRight = new Pawn("white", 5, 5);
        board.setPieceAt(5, 3, whitePawnLeft);
        board.setPieceAt(5, 5, whitePawnRight);

        // Test
        List<Board.Move> moves = board.getValidMoves(6, 4); // Получаем доступные ходы для белой пешки

        // Ожидаемый ход вперед
        Board.Move expectedForwardMove = new Board.Move(pawn, null, 6, 4, 5, 4);

        // Проверяем с использованием anyMatch, что диагональных захватов нет, а ход вперед есть
        assertTrue(moves.stream().anyMatch(move -> move.equals(expectedForwardMove)),
                "Pawn should move one step forward to (5, 4)");
        assertFalse(moves.stream().anyMatch(move -> move.getCapturedPiece() == whitePawnLeft),
                "Pawn should not capture on (5, 3) because it has the same color");
        assertFalse(moves.stream().anyMatch(move -> move.getCapturedPiece() == whitePawnRight),
                "Pawn should not capture on (5, 5) because it has the same color");
    }


    @Test
    public void testBlackPawnMoveForwardTwoStepsFromStartingPosition() {
        // Setup
        Pawn pawn = new Pawn("black", 1, 4);
        Piece[][] board = new Piece[8][8];

        // Test
        List<Board.Move> moves = pawn.getPotentialMoves(1, 4, board);

        // Verify
        assertEquals(2, moves.size());
        Board.Move expectedMoveOne = new Board.Move(pawn, null, 1, 4, 2, 4);
        Board.Move expectedMoveTwo = new Board.Move(pawn, null, 1, 4, 3, 4);
        assertEquals(expectedMoveOne, moves.get(0));
        assertEquals(expectedMoveTwo, moves.get(1));
    }
}
