package view;

import model.*;
import model.Board.Move;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//public class GameView extends JFrame {
//    private ChessBoardPanel boardPanel; // Панель для отображения доски
//    private Board board; // Модель доски
//    private boolean gameEnded = false; // Новый флаг состояния игры
//    private int selectedX = -1, selectedY = -1; // Координаты выбранной фигуры
//
//    public GameView(boolean againstComputer) {
//        setTitle("Шахматы");
//        setSize(600, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        board = new Board();
//        boardPanel = new ChessBoardPanel();
//        boardPanel.updateBoard(board.getBoardArray()); // Отобразить начальную позицию
//        setLayout(new BorderLayout());
//        add(boardPanel, BorderLayout.CENTER);
//
//        setupListeners();
//        setupButtons();
//
//        // Центрируем окно относительно экрана
//        setLocationRelativeTo(null);
//
//        setVisible(true);
//    }
//
//    private void setupButtons() {
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout());
//
//        // Кнопка отмены хода
//        JButton undoButton = new JButton("Отменить ход");
//        undoButton.addActionListener(e -> {
//            board.undoMove();
//            gameEnded = false;
//            boardPanel.updateBoard(board.getBoardArray());
//            System.out.println("Ход отменен");
//        });
//        buttonPanel.add(undoButton);
//
//        // Кнопка сохранения партии
//        JButton saveButton = new JButton("Сохранить партию");
//        saveButton.addActionListener(e -> {
//            // Создаем объект JFileChooser
//            JFileChooser fileChooser = new JFileChooser();
//
//            // Задаем начальный путь
//            String projectPath = System.getProperty("/CHESS"); // Корневая директория проекта
//            File myGamesDir = new File(projectPath, "MyGames");
//            if (!myGamesDir.exists()) {
//                myGamesDir.mkdirs(); // Создаем папку, если она не существует
//            }
//            fileChooser.setCurrentDirectory(myGamesDir);
//
//            // Открываем диалог сохранения
//            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
//                board.saveGameToNotationFile(fileChooser.getSelectedFile().getPath());
//                System.out.println("Партия сохранена");
//            }
//        });
//        buttonPanel.add(saveButton);
//
//        // Кнопка загрузки партии
//        JButton loadButton = new JButton("Загрузить партию");
//        loadButton.addActionListener(e -> {
//            // Создаем объект JFileChooser
//            JFileChooser fileChooser = new JFileChooser();
//
//            // Задаем начальный путь
//            String projectPath = System.getProperty("/CHESS"); // Корневая директория проекта
//            File myGamesDir = new File(projectPath, "MyGames");
//            if (!myGamesDir.exists()) {
//                myGamesDir.mkdirs(); // Создаем папку, если она не существует
//            }
//            fileChooser.setCurrentDirectory(myGamesDir);
//
//            // Открываем диалог загрузки
//            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//                board.loadGameFromNotationFile(fileChooser.getSelectedFile().getPath());
//                boardPanel.updateBoard(board.getBoardArray());
//                System.out.println("Партия загружена");
//            }
//        });
//        buttonPanel.add(loadButton);
//
//        add(buttonPanel, BorderLayout.SOUTH);
//    }
//
//    public void loadGame(String filePath) {
//        board.loadGameFromNotationFile(filePath); // Загрузка модели доски из файла
//        boardPanel.updateBoard(board.getBoardArray()); // Отображение загруженного состояния
//        System.out.println("Загружена партия из файла: " + filePath);
//    }
//
//    private void setupListeners() {
//        JButton[][] cells = boardPanel.getCells();
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                int x = i, y = j;
//                cells[i][j].addActionListener(e -> handleCellClick(x, y));
//            }
//        }
//    }
//
//
//    public void displayGameOver(String winnerColor) {
//        String message = "Игра окончена! Победитель: " + winnerColor;
//        javax.swing.JOptionPane.showMessageDialog(null, message, "Игра завершена", javax.swing.JOptionPane.INFORMATION_MESSAGE);
//    }
//
//
//    private void handleCellClick(int x, int y) {
//        if (gameEnded) {
//            System.out.println("Игра завершена. Невозможно выполнить ход.");
//            return; // Блокируем взаимодействие после завершения игры
//        }
//
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
//                checkGameEnd(); // Проверяем, завершилась ли игра
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
//
//    private void checkGameEnd() {
//        int endValue = board.checkGameEnd();
//
//        if (endValue == -2) {
//            JOptionPane.showMessageDialog(this, "Шах и мат! Черные выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//            endGame(); // Завершаем игру без сброса
//        } else if (endValue == 2) {
//            JOptionPane.showMessageDialog(this, "Шах и мат! Белые выиграли!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//            endGame(); // Завершаем игру без сброса
//        } else if (endValue == -1) {
//            JOptionPane.showMessageDialog(this, "Пат (У белых нет ходов)! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//            endGame(); // Завершаем игру без сброса
//        } else if (endValue == 1) {
//            JOptionPane.showMessageDialog(this, "Пат (У черных нет ходов)! Ничья!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
//            endGame(); // Завершаем игру без сброса
//        }
//    }
//
//    private void endGame() {
//        gameEnded = true; // Устанавливаем флаг
//        System.out.println("Игра завершена! Ожидание новой игры.");
//    }
//
//    public void resetGame() {
//        board = new Board(); // Пересоздаем модель доски
//        boardPanel.updateBoard(board.getBoardArray()); // Обновляем отображение
//        selectedX = -1;
//        selectedY = -1;
//        gameEnded = false; // Сбрасываем состояние игры
//        System.out.println("Игра была сброшена!");
//    }
//
//}

public class GameView extends JFrame {
    private boolean againstComputer; // Режим против компьютера
    private boolean gameEnded = false; // Статус окончания игры
    private Board board;
    private ChessBoardPanel boardPanel;
    private ChessAI chessAI; // Новый класс логики компьютера
    private int selectedX = -1, selectedY = -1; // Координаты выбранной фигуры

    public GameView(boolean againstComputer) {
        this.againstComputer = againstComputer;
        setTitle("Шахматы");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new Board();
        boardPanel = new ChessBoardPanel();
        boardPanel.updateBoard(board.getBoardArray()); // Отобразить начальную позицию
        chessAI = new ChessAI(board); // Инициализация логики ИИ

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);

        setupListeners(); // Обработчики действий
        setupButtons();   // Нижняя панель кнопок

        // Центрируем окно относительно экрана
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

        // Кнопка сохранения партии
        JButton saveButton = new JButton("Сохранить партию");
        saveButton.addActionListener(e -> {
            // Создаем объект JFileChooser
            JFileChooser fileChooser = new JFileChooser();

            // Задаем начальный путь
            String projectPath = System.getProperty("/CHESS"); // Корневая директория проекта
            File myGamesDir = new File(projectPath, "MyGames");
            if (!myGamesDir.exists()) {
                myGamesDir.mkdirs(); // Создаем папку, если она не существует
            }
            fileChooser.setCurrentDirectory(myGamesDir);

            // Открываем диалог сохранения
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                board.saveGameToNotationFile(fileChooser.getSelectedFile().getPath());
                System.out.println("Партия сохранена");
            }
        });
        buttonPanel.add(saveButton);

        // Кнопка загрузки партии
        JButton loadButton = new JButton("Загрузить партию");
        loadButton.addActionListener(e -> {
            // Создаем объект JFileChooser
            JFileChooser fileChooser = new JFileChooser();

            // Задаем начальный путь
            String projectPath = System.getProperty("/CHESS"); // Корневая директория проекта
            File myGamesDir = new File(projectPath, "MyGames");
            if (!myGamesDir.exists()) {
                myGamesDir.mkdirs(); // Создаем папку, если она не существует
            }
            fileChooser.setCurrentDirectory(myGamesDir);

            // Открываем диалог загрузки
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

                checkGameEnd();

                // Если режим против компьютера, выполняем ход ИИ
                if (againstComputer && !gameEnded) {
                    performComputerMove(); // Ход компьютера
                }
            } else {
                System.out.println("Неверный ход!");
            }

            // Сброс выбора и подсветки
            selectedX = -1;
            selectedY = -1;
            boardPanel.clearHighlights();
        }
    }

    private void performComputerMove() {
        System.out.println("Компьютер выполняет ход...");

        // Выполнить ход через ChessAI
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

    private void endGame() {
        gameEnded = true; // Устанавливаем флаг
        System.out.println("Игра завершена! Ожидание новой игры.");
    }
}
