package model;

import java.util.ArrayList;
import java.util.List;

import model.Board.Move;

public class Knight extends Piece {
    public Knight(String color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public boolean isValidMove(int targetX, int targetY, Board board) {
        if (!isInBounds(targetX, targetY)) {
            return false;
        }

        if (this.getColor() != board.getCurrentPlayerColor()) {
            return false;
        }

        int dx = Math.abs(this.x - targetX);
        int dy = Math.abs(this.y - targetY);

        if (!(dx == 2 && dy == 1) && !(dx == 1 && dy == 2)) {
            return false;
        }

        if (this.getColor() == board.getPieceAt(targetX, targetY).getColor()) {
            return false;
        }

        return true;
        // return board.isPathClear(this.x, this.y, targetX, targetY);
        // int direction = color.equals("white") ? -1 : 1;
        // return targetX == x + direction && targetY == y && board.getPieceAt(targetX, targetY) == null;
    }

    @Override
    public boolean canAttack(int x, int y, Board board) {
        // Конь угрожает "г-образно", без преград
        int deltaX = Math.abs(this.x - x);
        int deltaY = Math.abs(this.y - y);
        return (deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2);
    }

    @Override
    protected String getType() {
        // TODO Auto-generated method stub
        return "N";
    }
    @Override
    public List<Move> getPotentialMoves(int x, int y, Piece[][] board) {
        List<Move> moves = new ArrayList<>();
        int[][] knightMoves = {
                {-2, -1}, {-2, 1},  
            {-1, -2},           {-1, 2},
            {1, -2},            {1, 2},
                {2, -1}, { 2, 1}
        };

        for (int[] move : knightMoves) {
            int newX = x + move[0];
            int newY = y + move[1];
            if (isInBounds(newX, newY)) {
                if (board[newX][newY] == null || !board[newX][newY].getColor().equals(getColor())) {
                    moves.add(new Move(this, board[newX][newY], x, y, newX, newY));
                }
            }
        }

        return moves;
    }

    
}
