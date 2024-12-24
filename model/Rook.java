package model;

import java.util.ArrayList;
import java.util.List;

import model.Board.Move;

public class Rook extends Piece {
    public Rook(String color, int x, int y) {
        super(color, x, y);
    }

    @Override
    protected String getType() {
        // TODO Auto-generated method stub
        return "R";
    }

    @Override
    public boolean canAttack(int x, int y, Board board) {
        // Ладья атакует только по прямой линии (горизонтали или вертикали)
        if (this.x == x || this.y == y) {
            // Проверяем наличие преград
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

    public static boolean isPathClear(Board board, int fromX, int fromY, int toX, int toY) {
        int deltaX = Integer.signum(toX - fromX);
        int deltaY = Integer.signum(toY - fromY);
        int newX = fromX + deltaX, newY = fromY + deltaY;
    
        while (newX != toX || newY != toY) {
            if (board.getPieceAt(newX, newY) != null) {
                return false;
            }
            newX += deltaX;
            newY += deltaY;
        }
        return true;
    }

    @Override
    public List<Move> getPotentialMoves(int x, int y, Piece[][] board) {
        List<Move> moves = new ArrayList<>();

        // Движение вверх, вниз, влево, вправо
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newX = x + dir[0], newY = y + dir[1];
            while (isInBounds(newX, newY)) {
                if (board[newX][newY] == null) {
                    moves.add(new Move(this, null, x, y, newX, newY));
                } else if (!board[newX][newY].getColor().equals(getColor())) {
                    moves.add(new Move(this, board[newX][newY], x, y, newX, newY));
                    break; // Нельзя перепрыгивать через фигуры
                } else {
                    break; // Блокировка своей фигурой
                }
                newX += dir[0];
                newY += dir[1];
            }
        }

        return moves;
    }
}
