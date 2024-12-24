package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public class ChessBoardPanel extends JPanel {
    private JButton[][] cells = new JButton[8][8];
    private List<Point> highlightedCells = new ArrayList<>();

    public ChessBoardPanel() {
        setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(60, 60));
                cell.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                cell.setOpaque(true);
                cell.setBorderPainted(false);
                cells[i][j] = cell;
                add(cells[i][j]);
            }
        }
    }

    public JButton[][] getCells() {
        return cells;
    }

    public void updateBoard(Piece[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton cell = cells[i][j];
                Piece piece = board[i][j];
                if (piece != null) {
                    String imageName = piece.getColor() + "_" + piece.getClass().getSimpleName().toLowerCase() + ".png";
                    cell.setIcon(ImageLoader.getImage(imageName));
                } else {
                    cell.setIcon(null);
                }
            }
        }
    }

    // Подсветить ячейки
    public void highlightCells(List<Point> points) {
        clearHighlights(); // Убираем старую подсветку
        for (Point point : points) {
            int x = point.x;
            int y = point.y;
            cells[x][y].setBackground(Color.YELLOW); // Устанавливаем цвет подсветки
        }
        highlightedCells = points; // Сохраняем подсвеченные ячейки
    }

    // Очистить подсветку
    public void clearHighlights() {
        for (Point point : highlightedCells) {
            int x = point.x;
            int y = point.y;
            cells[x][y].setBackground((x + y) % 2 == 0 ? Color.WHITE : Color.GRAY); // Восстанавливаем цвет клетки
        }
        highlightedCells.clear();
    }
}
