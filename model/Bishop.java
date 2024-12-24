package model;

import java.util.ArrayList;
import java.util.List;

import model.Board.Move;

public class Bishop extends Piece {
    public Bishop(String color, int x, int y) {
        super(color, x, y);
    }


    public static boolean isPathClear(Board board, int startX, int startY, int endX, int endY) {
        int deltaX = Math.abs(endX - startX);
        int deltaY = Math.abs(endY - startY);


        if (deltaX != deltaY) {
            return false;
        }

        int stepX = (endX > startX) ? 1 : -1;
        int stepY = (endY > startY) ? 1 : -1;

        int currentX = startX + stepX;
        int currentY = startY + stepY;

        while (currentX != endX && currentY != endY) {
            if (board.getPieceAt(currentX, currentY) != null) {
                return false;
            }
            currentX += stepX;
            currentY += stepY;
        }

        return true;
    }


    @Override
    public boolean canAttack(int x, int y, Board board) {
        int deltaX = Math.abs(this.x - x);
        int deltaY = Math.abs(this.y - y);
        if (deltaX != deltaY) return false;

        int stepX = (x > this.x) ? 1 : -1;
        int stepY = (y > this.y) ? 1 : -1;
        int currentX = this.x + stepX;
        int currentY = this.y + stepY;

        while (currentX != x && currentY != y) {
            if (board.getPieceAt(currentX, currentY) != null) return false;
            currentX += stepX;
            currentY += stepY;
        }
        return true;
    }

    @Override
    protected String getType() {
        // TODO Auto-generated method stub
        return "B";
    }

    @Override
    public List<Move> getPotentialMoves(int x, int y, Piece[][] board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};  // For Bishop

        for (int[] dir : directions) {
            int newX = x + dir[0], newY = y + dir[1];
            while (isInBounds(newX, newY)) {
                if (board[newX][newY] == null) {
                    moves.add(new Move(this, null, x, y, newX, newY));
                } else if (board[newX][newY].getColor() != getColor()) {
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
}
