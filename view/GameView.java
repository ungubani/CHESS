package view;

import model.*;
import model.Board.Move;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JFrame {
    private ChessBoardPanel boardPanel; // Панель для отображения доски
    private Board board; // Модель доски
    private int selectedX = -1, selectedY = -1; // Координаты выбранной фигуры

    public GameView(boolean againstComputer) {
        setTitle("Шахматы");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new Board();
        boardPanel = new ChessBoardPanel();
        boardPanel.updateBoard(board.getBoardArray()); // Отобразить начальную позицию
        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);

        setupListeners();
        setupButtons();

        setVisible(true);
    }

    private void setupButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Кнопка отмены хода
        JButton undoButton = new JButton("Отменить ход");
        undoButton.addActionListener(e -> {
            board.undoMove();
            boardPanel.updateBoard(board.getBoardArray());
            System.out.println("Ход отменен");
        });
        buttonPanel.add(undoButton);

        // Кнопка сохранения партии
        JButton saveButton = new JButton("Сохранить партию");
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                board.saveGameToNotationFile(fileChooser.getSelectedFile().getPath());
                System.out.println("Партия сохранена");
            }
        });
        buttonPanel.add(saveButton);

        // Кнопка загрузки партии
        JButton loadButton = new JButton("Загрузить партию");
        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                board.loadGameFromNotationFile(fileChooser.getSelectedFile().getPath());
                boardPanel.updateBoard(board.getBoardArray());
                System.out.println("Партия загружена");
            }
        });
        buttonPanel.add(loadButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        JButton[][] cells = boardPanel.getCells();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int x = i, y = j;
                cells[i][j].addActionListener(e -> handleCellClick(x, y));
            }
        }
    }


    public void displayGameOver(String winnerColor) {
        String message = "Игра окончена! Победитель: " + winnerColor;
        javax.swing.JOptionPane.showMessageDialog(null, message, "Игра завершена", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }


    private void handleCellClick(int x, int y) {
        if (gameEnded) {
            System.out.println("Игра завершена. Невозможно выполнить ход.");
            return; // Блокируем взаимодействие после завершения игры
        }

        if (selectedX == -1 && selectedY == -1) {
            // Выбор фигуры
            Piece piece = board.getPieceAt(x, y);
            if (piece != null) {
                selectedX = x;
                selectedY = y;
                System.out.println("Выбрана фигура: " + piece.getClass().getSimpleName() + " на " + x + "," + y);

                // Подсветка возможных ходов
                List<Move> possibleMoves = board.getValidMoves(selectedX, selectedY);
                List<Point> points = new ArrayList<>();
                for (Move move : possibleMoves) {
                    points.add(new Point(move.toX, move.toY));
                }
                boardPanel.highlightCells(points);
            }
        } else {
            // Попытка совершить ход
            if (board.movePiece(selectedX, selectedY, x, y)) {
                boardPanel.updateBoard(board.getBoardArray());
                System.out.println("Ход выполнен: с " + selectedX + "," + selectedY + " на " + x + "," + y);

                checkGameEnd(); // Проверяем, завершилась ли игра
            } else {
                System.out.println("Неверный ход!");
            }

            // Сброс выбора и подсветки
            selectedX = -1;
            selectedY = -1;
            boardPanel.clearHighlights();
        }
    }

    private void checkGameEnd() {
        int endValue = board.checkGameEnd();

        if (endValue == -2) {
            JOptionPane.showMessageDialog(this, "Шах и мат! Черные выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
            endGame(); // Завершаем игру без сброса
        } else if (endValue == 2) {
            JOptionPane.showMessageDialog(this, "Шах и мат! Белые выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
            endGame(); // Завершаем игру без сброса
        } else if (endValue == -1) {
            JOptionPane.showMessageDialog(this, "Пат (У белых нет ходов)! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
            endGame(); // Завершаем игру без сброса
        } else if (endValue == 1) {
            JOptionPane.showMessageDialog(this, "Пат (У черных нет ходов)! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
            endGame(); // Завершаем игру без сброса
        }
    }

//    private void checkGameEnd() {
//        if (board.getCurrentPlayerColor().equals("white")) {
//            if (board.isKingInCheck("white") && board.noValidMoves("white")) {
//                JOptionPane.showMessageDialog(this, "Шах и мат! Черные выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//                endGame(); // Завершаем игру без сброса
//            } else if (!board.isKingInCheck("white") && board.noValidMoves("white")) {
//                JOptionPane.showMessageDialog(this, "Пат! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//                endGame(); // Завершаем игру без сброса
//            }
//        } else {
//            if (board.isKingInCheck("black") && board.noValidMoves("black")) {
//                JOptionPane.showMessageDialog(this, "Шах и мат! Белые выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//                endGame(); // Завершаем игру без сброса
//            } else if (!board.isKingInCheck("black") && board.noValidMoves("black")) {
//                JOptionPane.showMessageDialog(this, "Пат! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//                endGame(); // Завершаем игру без сброса
//            }
//        }
//    }

//        if (board.isKingInCheck("white") && board.noValidMoves("white")) {
//            JOptionPane.showMessageDialog(this, "Шах и мат! Черные выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//            endGame(); // Завершаем игру без сброса
//        } else if (board.isKingInCheck("black") && board.noValidMoves("black")) {
//            JOptionPane.showMessageDialog(this, "Шах и мат! Белые выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//            endGame(); // Завершаем игру без сброса
//        } else if (!board.isKingInCheck("white") && board.noValidMoves("white")) {
//            JOptionPane.showMessageDialog(this, "Пат! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//            endGame(); // Завершаем игру без сброса
//        } else if (!board.isKingInCheck("black") && board.noValidMoves("black")) {
//            JOptionPane.showMessageDialog(this, "Пат! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//            endGame(); // Завершаем игру без сброса
//        }


    private boolean gameEnded = false; // Новый флаг состояния игры

    private void endGame() {
        gameEnded = true; // Устанавливаем флаг
        System.out.println("Игра завершена! Ожидание новой игры.");
    }

    public void resetGame() {
        board = new Board(); // Пересоздаем модель доски
        boardPanel.updateBoard(board.getBoardArray()); // Обновляем отображение
        selectedX = -1;
        selectedY = -1;
        gameEnded = false; // Сбрасываем состояние игры
        System.out.println("Игра была сброшена!");
    }

//    private void handleCellClick(int x, int y) {
//        if (selectedX == -1 && selectedY == -1) {
//            // Выбор фигуры
//            Piece piece = board.getPieceAt(x, y);
//            if (piece != null) {
//                selectedX = x;
//                selectedY = y;
//                System.out.println("Выбрана фигура: " + piece.getClass().getSimpleName() + " на " + x + "," + y);
//
//                // Подсветка возможных ходов
//                List<Move> possibleMoves = board.getValidMoves(selectedX, selectedY);
//                List<Point> points = new ArrayList<>();
//                for (Move move : possibleMoves) {
//                    points.add(new Point(move.toX, move.toY));
//                }
//                boardPanel.highlightCells(points);
//            }
//        } else {
//            // Попытка совершить ход
//            if (board.movePiece(selectedX, selectedY, x, y)) {
//                boardPanel.updateBoard(board.getBoardArray());
//                System.out.println("Ход выполнен: с " + selectedX + "," + selectedY + " на " + x + "," + y);
//
//                checkGameEnd();
//            } else {
//                System.out.println("Неверный ход!");
//            }
//
//            // Сброс выбора и подсветки
//            selectedX = -1;
//            selectedY = -1;
//            boardPanel.clearHighlights();
//        }
//    }
//    private void checkGameEnd() {
//        if (board.isKingInCheck("white") && board.noValidMoves("white")) {
//            JOptionPane.showMessageDialog(this, "Шах и мат! Черные выиграли!");
//            resetGame();
//        } else if (board.isKingInCheck("black") && board.noValidMoves("black")) {
//            JOptionPane.showMessageDialog(this, "Шах и мат! Белые выиграли!");
//            resetGame();
//        } else if (board.noValidMoves("white") && board.noValidMoves("black")) {
//            JOptionPane.showMessageDialog(this, "Пат! Ничья!");
//            resetGame();
//        }
//    }
//    private void resetGame() {
//        board = new Board(); // Пересоздаем модель доски
//        boardPanel.updateBoard(board.getBoardArray()); // Обновляем отображение
//        System.out.println("Игра была сброшена!");
//    }
}
