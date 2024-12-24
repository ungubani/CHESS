package model;

import java.util.ArrayList;
import java.util.List;

import model.Board.Move;

public class Knight extends Piece {
    public Knight(String color, int x, int y) {
        super(color, x, y);
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
