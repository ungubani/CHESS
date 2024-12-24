package model;

import java.util.ArrayList;
import java.util.List;

import model.Board.Move;

public class Queen extends Piece{
    public Queen(String color, int x, int y) {
        super(color, x, y);
    }

    @Override
    protected String getType() {
        // TODO Auto-generated method stub
        return "Q";
    }

    @Override
    public List<Move> getPotentialMoves(int x, int y, Piece[][] board) {
        List<Move> moves = new ArrayList<>();

        int[][] rookDirections = {
            {-1, 0}, 
        {0, -1},  {0, 1},
            { 1, 0}
        };
        for (int[] dir : rookDirections) {
            int newX = x + dir[0], newY = y + dir[1];
            while (isInBounds(newX, newY)) {
                if (board[newX][newY] == null) {
                    moves.add(new Move(this, null, x, y, newX, newY));
                } else if (!board[newX][newY].getColor().equals(getColor())) {
                    moves.add(new Move(this, board[newX][newY], x, y, newX, newY));
                    break;
                } else {
                    break;
                }
                newX += dir[0];
                newY += dir[1];
            }
        }

        int[][] bishopDirections = {
                {-1, -1},   {-1, 1},

                {1, -1},    {1, 1}
        };

        for (int[] dir : bishopDirections) {
            int newX = x + dir[0], newY = y + dir[1];
            while (isInBounds(newX, newY)) {
                if (board[newX][newY] == null) {
                    moves.add(new Move(this, null, x, y, newX, newY));
                } else if (!board[newX][newY].getColor().equals(getColor())) {
                    moves.add(new Move(this, board[newX][newY], x, y, newX, newY));
                    break;
                } else {
                    break;
                }
                newX += dir[0];
                newY += dir[1];
            }
        }

        return moves;
    }

    @Override
    public boolean canAttack(int x, int y, Board board) {
        int deltaX = Math.abs(this.x - x);
        int deltaY = Math.abs(this.y - y);

        if (deltaX == deltaY || this.x == x || this.y == y) {
            int stepX = Integer.compare(x, this.x); // -1, 0 или 1
            int stepY = Integer.compare(y, this.y); // -1, 0 или 1
            int currentX = this.x + stepX;
            int currentY = this.y + stepY;

            while (currentX != x || currentY != y) {
                if (board.getPieceAt(currentX, currentY) != null) {
                    return false;
                }
                currentX += stepX;
                currentY += stepY;
            }
            return true;
        }
        return false;
    }
}
