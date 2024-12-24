package model;

import java.util.ArrayList;
import java.util.List;

import model.Board.Move;

public class King extends Piece{
    public King(String color, int x, int y) {
        super(color, x, y);
    }


    @Override
    public boolean canAttack(int x, int y, Board board) {
        if (!isInBounds(x, y)) {
            return false;
        }

        int deltaX = Math.abs(this.x - x);
        int deltaY = Math.abs(this.y - y);
        if (deltaX == 0 && deltaY == 0) {
            return false;
        }

        return (deltaX <= 1 && deltaY <= 1);
    }

    @Override
    protected String getType() {
        // TODO Auto-generated method stub
        return "K";
    }

    @Override
    public List<Move> getPotentialMoves(int x, int y, Piece[][] board) {
        List<Move> moves = new ArrayList<>();
        int[][] kingMoves = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
        };

        for (int[] move : kingMoves) {
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
