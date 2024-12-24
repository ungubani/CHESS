package model;

import java.util.ArrayList;
import java.util.List;

import model.Board.Move;

public class Pawn extends Piece {
    public Pawn(String color, int x, int y) {
        super(color, x, y);
    }


    @Override
    public boolean canAttack(int x, int y, Board board) {
        int direction = this.color.equals("white") ? -1 : 1;
        return (x == this.x + direction) && (Math.abs(this.y - y) == 1);
    }

    @Override
    protected String getType() {
        // TODO Auto-generated method stub
        return "P";
    }

    @Override
    public List<Move> getPotentialMoves(int x, int y, Piece[][] board) {

        List<Move> moves = new ArrayList<>();
        int direction = getColor().equals("white") ? -1 : 1;

        if (isInBounds(x + direction, y) && board[x + direction][y] == null) {
            moves.add(new Move(this, null, x, y, x + direction, y));

            if ((getColor().equals("white") && x == 6 || getColor().equals("black") && x == 1) 
                && board[x + 2 * direction][y] == null) {
                moves.add(new Move(this, null, x, y, x + 2 * direction, y));
            }
        }

        if (isInBounds(x + direction, y - 1) && board[x + direction][y - 1] != null
            && !board[x + direction][y - 1].getColor().equals(getColor())) {
            moves.add(new Move(this, board[x + direction][y - 1], x, y, x + direction, y - 1));
        }
        if (isInBounds(x + direction, y + 1) && board[x + direction][y + 1] != null
            && !board[x + direction][y + 1].getColor().equals(getColor())) {
            moves.add(new Move(this, board[x + direction][y + 1], x, y, x + direction, y + 1));
        }

        return moves;
    }
}

