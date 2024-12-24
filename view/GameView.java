package view;

import model.*;
import model.Board.Move;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class GameView extends JFrame {
    private boolean againstComputer;
    private boolean gameEnded = false;
    private Board board;
    private ChessBoardPanel boardPanel;
    private ChessAI chessAI;
    private int selectedX = -1, selectedY = -1;

    public GameView(boolean againstComputer) {
        this.againstComputer = againstComputer;
        setTitle("Шахматы");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new Board();
        boardPanel = new ChessBoardPanel();
        boardPanel.updateBoard(board.getBoardArray());
        chessAI = new ChessAI(board);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);

        setupListeners();
        setupButtons();

        setLocationRelativeTo(null);

        setVisible(true);
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

    private void setupButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Кнопка отмены хода
        JButton undoButton = new JButton("Отменить ход");
        undoButton.addActionListener(e -> {
            board.undoMove();
            gameEnded = false;
            boardPanel.updateBoard(board.getBoardArray());
            System.out.println("Ход отменен");
        });
        buttonPanel.add(undoButton);

        JButton saveButton = new JButton("Сохранить партию");
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            String projectPath = System.getProperty("/CHESS");
            File myGamesDir = new File(projectPath, "MyGames");
            if (!myGamesDir.exists()) {
                myGamesDir.mkdirs();
            }
            fileChooser.setCurrentDirectory(myGamesDir);

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                board.saveGameToNotationFile(fileChooser.getSelectedFile().getPath());
                System.out.println("Партия сохранена");
            }
        });
        buttonPanel.add(saveButton);

        JButton loadButton = new JButton("Загрузить партию");
        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            String projectPath = System.getProperty("/CHESS");
            File myGamesDir = new File(projectPath, "MyGames");
            if (!myGamesDir.exists()) {
                myGamesDir.mkdirs();
            }
            fileChooser.setCurrentDirectory(myGamesDir);

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                board.loadGameFromNotationFile(fileChooser.getSelectedFile().getPath());
                boardPanel.updateBoard(board.getBoardArray());
                System.out.println("Партия загружена");
            }
        });
        buttonPanel.add(loadButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleCellClick(int x, int y) {
        if (gameEnded) {
            System.out.println("Игра завершена. Невозможно выполнить ход.");
            return;
        }

        if (selectedX == -1 && selectedY == -1) {
            Piece piece = board.getPieceAt(x, y);
            if (piece != null) {
                selectedX = x;
                selectedY = y;
                System.out.println("Выбрана фигура: " + piece.getClass().getSimpleName() + " на " + x + "," + y);

                List<Move> possibleMoves = board.getValidMoves(selectedX, selectedY);
                List<Point> points = new ArrayList<>();
                for (Move move : possibleMoves) {
                    points.add(new Point(move.toX, move.toY));
                }
                boardPanel.highlightCells(points);
            }
        } else {
            if (board.movePiece(selectedX, selectedY, x, y)) {
                boardPanel.updateBoard(board.getBoardArray());
                System.out.println("Ход выполнен: с " + selectedX + "," + selectedY + " на " + x + "," + y);

                checkGameEnd();

                if (againstComputer && !gameEnded) {
                    performComputerMove();
                }
            } else {
                System.out.println("Неверный ход!");
            }

            selectedX = -1;
            selectedY = -1;
            boardPanel.clearHighlights();
        }
    }

    private void performComputerMove() {
        System.out.println("Компьютер выполняет ход...");

        if (chessAI.performMove()) {
            boardPanel.updateBoard(board.getBoardArray());
            System.out.println("Компьютер завершил ход.");
            checkGameEnd();
        } else {
            System.out.println("Компьютер не может сделать ход (возможно, пат или мат).");
        }
    }

    private void checkGameEnd() {
        int endValue = board.checkGameEnd();

        if (endValue == -2) {
            JOptionPane.showMessageDialog(this, "Шах и мат! Черные выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
            endGame();
        } else if (endValue == 2) {
            JOptionPane.showMessageDialog(this, "Шах и мат! Белые выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
            endGame();
        } else if (endValue == -1) {
            JOptionPane.showMessageDialog(this, "Пат (У белых нет ходов)! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
            endGame();
        } else if (endValue == 1) {
            JOptionPane.showMessageDialog(this, "Пат (У черных нет ходов)! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
            endGame();
        }
    }

    private void endGame() {
        gameEnded = true;
        System.out.println("Игра завершена! Ожидание новой игры.");
    }
}
