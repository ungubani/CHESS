package model;

import java.util.List;

import model.Board.Move;

public abstract class Piece implements Cloneable {
    protected String color; // "white" или "black"
    protected int x, y;     // Координаты фигуры

    public Piece(String color, int x, int y) {
        if (!isInBounds(x, y)) {
            System.out.println("Для выбранной фигуры неверные значения координат");
        }
        this.color = color;
        // this.colorCell = ((x + y) % 2 == 0) ? "white" : "black"; 
        this.x = x;
        this.y = y;
    }

    public abstract boolean canAttack(int x, int y, Board board);

    public void setPosition(int newX, int newY) {
        if (isInBounds(newX, newY)) {
            this.x = newX;
            this.y = newY;
        }
        else {
            System.out.println("Для выбранной фигуры неверные значения координат");
        }
    };

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); // Для простых классов достаточно поверхностного клонирования
    }

    public abstract boolean isValidMove(int targetX, int targetY, Board board);

    public String getColor() { return color; }
    public String getColorCell() { return ((x + y) % 2 == 0) ? "white" : "black"; }

    protected abstract String getType();

    public abstract List<Move> getPotentialMoves(int x, int y, Piece[][] board);

    public String pieceInfo() {
        return "Фигура: " + getType() + ", Цвет: " + color + ", Координаты: (" + x + ", " + y + ")";
    }
    
}

