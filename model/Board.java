package model;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Board implements Cloneable {
    static public class Move {
        public Piece movedPiece;
        public Piece capturedPiece;
        public int fromX, fromY, toX, toY;
    
        public Move(Piece movedPiece, Piece capturedPiece, int fromX, int fromY, int toX, int toY) {
            this.movedPiece = movedPiece;
            this.capturedPiece = capturedPiece;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true; // Сравнение ссылок
            if (obj == null || getClass() != obj.getClass()) return false; // Проверка на совместимость классов

            Move move = (Move) obj; // Приведение типа
            // Сравниваем исходные и конечные координаты и фигуру
            return fromX == move.fromX &&
                fromY == move.fromY &&
                toX == move.toX &&
                toY == move.toY &&
                (movedPiece != null ? movedPiece.equals(move.movedPiece) : move.movedPiece == null);
        }


        // Генерирует хэш-код на основе координат и перемещаемой фигуры, что обеспечивает корректную работу в коллекциях.
        @Override
        public int hashCode() {
            // Генерируем хэш на основе всех полей, задействованных в equals()
            int result = Integer.hashCode(fromX);
            result = 31 * result + Integer.hashCode(fromY);
            result = 31 * result + Integer.hashCode(toX);
            result = 31 * result + Integer.hashCode(toY);
            result = 31 * result + (movedPiece != null ? movedPiece.hashCode() : 0);
            return result;
        }

        public Piece getCapturedPiece() {
            return capturedPiece;
        }
    }

    private Piece[][] board = new Piece[8][8];
    private String currentPlayerColor = "";
    public Stack<Move> moveHistory = new Stack<Move>();


    public Board() {
        setupInitialPosition();
    }
    
    private void setupInitialPosition() {
        currentPlayerColor = "white";
        // Пешки
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("black", 1, i);
            board[6][i] = new Pawn("white", 6, i);
        }
        // Ладьи
        board[0][0] = new Rook("black", 0, 0);
        board[0][7] = new Rook("black", 0, 7);
        board[7][0] = new Rook("white", 7, 0);
        board[7][7] = new Rook("white", 7, 7);
        
        board[0][1] = new Knight("black", 0, 1);
        board[0][6] = new Knight("black", 0, 6);
        board[7][1] = new Knight("white", 7, 1);
        board[7][6] = new Knight("white", 7, 6);

        board[0][2] = new Bishop("black", 0, 2);
        board[0][5] = new Bishop("black", 0, 5);
        board[7][2] = new Bishop("white", 7, 2);
        board[7][5] = new Bishop("white", 7, 5);

        board[0][3] = new Queen("black", 0, 3);
        board[7][3] = new Queen("white", 7, 3);

        board[0][4] = new King("black", 0, 4);
        board[7][4] = new King("white", 7, 4);
    }

    public Board(boolean isEmpty) {
        if (!isEmpty) {
            setupInitialPosition();
        } else {
            currentPlayerColor = "white";

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    board[i][j] = null;
                }
            }
        }
    }
    

    public Piece getPieceAt(int x, int y) {
        return board[x][y];
    }
    public void setPieceAt(int x, int y, Piece piece) {
        if (!isValidCoordinate(x, y)) {
            System.out.println("Некорректные координаты для установки фигуры");
            return;
        }
        board[x][y] = piece;
        piece.setPosition(x, y);
    }

    public Piece[][] getBoardArray() {
        return board;
    }

    public boolean movePiece(int fromX, int fromY, int toX, int toY) {
        List<Move> validMoves = getValidMoves(fromX, fromY);
        for (Move move : validMoves) {
            if (move.toX == toX && move.toY == toY) {
                applyMove(move);
                return true;
            }
        }
        return false; // Ход недопустим
    }

    
    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            board[lastMove.fromX][lastMove.fromY] = lastMove.movedPiece;
            lastMove.movedPiece.setPosition(lastMove.fromX, lastMove.fromY);

            board[lastMove.toX][lastMove.toY] = lastMove.capturedPiece;
            currentPlayerColor = lastMove.movedPiece.getColor();
//            currentPlayerColor = lastMove.movedPiece.getColor() == "white" ? "black" : "white";
        }
    }    


    public boolean isKingInCheck(String color) {
        int kingX = -1, kingY = -1;

        // Найдите короля
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPieceAt(i, j);
                if (piece instanceof King && piece.getColor().equals(color)) {
                    kingX = i;
                    kingY = j;
                    break;
                }
            }
        }

        // Проверьте, может ли любая фигура противника атаковать короля
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPieceAt(i, j);
                if (piece != null && !piece.getColor().equals(color)) {
                    if (piece.canAttack(kingX, kingY, this)) {
                        System.out.println("Шах королю " + color + " от " + piece.pieceInfo());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean noValidMoves(String color) {
        // Проходим по всей доске
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = getPieceAt(x, y);
                if (piece != null && piece.getColor().equals(color)) {
                    // Получаем все возможные ходы данной фигуры
                    List<Move> validMoves = getValidMoves(x, y);

                    // Проверяем каждый из возможных ходов
                    for (Move move : validMoves) {
                        // Сохраняем текущее состояние
                        applyMove(move);

                        // Проверяем, останется ли король под шахом
                        boolean stillInCheck = isKingInCheck(color);

                        // Откатываем ход
                        undoMove();

                        // Если найден хотя бы один ход, при котором король не в шахе
                        if (!stillInCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true; // Ни одного легального хода не найдено
    }

    public int checkGameEnd() {
        if (getCurrentPlayerColor().equals("white")) {
            if (isKingInCheck("white") && noValidMoves("white")) {
                return -2;
            } else if (!isKingInCheck("white") && noValidMoves("white")) {
                return -1;
            }
        } else {
            if (isKingInCheck("black") && noValidMoves("black")) {
                return 2;
            } else if (!isKingInCheck("black") && noValidMoves("black")) {
                return 1;
            }
        }

        return 0;
    }


    public void saveGameToNotationFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            int moveNumber = 1;
            for (int i = 0; i < moveHistory.size(); i += 2) {
                Move whiteMove = moveHistory.get(i); // Ход белых
                String whiteMoveNotation = convertMoveToNotation(whiteMove);

                String blackMoveNotation = "";
                if (i + 1 < moveHistory.size()) {
                    Move blackMove = moveHistory.get(i + 1); // Ход чёрных
                    blackMoveNotation = convertMoveToNotation(blackMove);
                }

                writer.write(moveNumber + ". " + whiteMoveNotation + (blackMoveNotation.isEmpty() ? "" : " " + blackMoveNotation));
                writer.newLine();
                moveNumber++;
            }
            System.out.println("Партия успешно сохранена в файл: " + filePath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String convertMoveToNotation(Move move) {
        String pieceSymbol = move.movedPiece.getType(); // Тип фигуры: K, Q, R, B, N, P
        String from = convertPositionToNotation(move.fromX, move.fromY); // Исходная клетка, например: "e2"
        String to = convertPositionToNotation(move.toX, move.toY); // Целевая клетка, например: "e4"

        // Проверяем, является ли это взятием
        boolean isCapture = move.capturedPiece != null;
        return (pieceSymbol.equals("P") ? from : pieceSymbol + from) + (isCapture ? "x" : "-") + to;
    }

    private String convertPositionToNotation(int x, int y) {
        char file = (char) ('a' + y); // Преобразование столбца в символ a-h
        int rank = 8 - x; // Преобразование строки в шахматный номер
        return "" + file + rank;
    }


    public void loadGameFromNotationFile(String filePath) {
        setupInitialPosition(); // Устанавливаем начальную позицию доски

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Читаем строку за строкой
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Разбиваем строку по пробелам: ["1.", "d2-d4", "d7-d5"]

                try {
                    // Если есть второй ход - белый
                    if (parts.length >= 2) {
                        String whiteMoveNotation = parts[1]; // Первый ход
                        Move whiteMove = parseNotationToMove(whiteMoveNotation); // Преобразуем нотацию в объект Move
                        applyMove(whiteMove); // Применяем ход на доску
                    }

                    // Если есть третий ход - чёрный
                    if (parts.length == 3) {
                        String blackMoveNotation = parts[2]; // Второй ход
                        Move blackMove = parseNotationToMove(blackMoveNotation); // Преобразуем нотацию в объект Move
                        applyMove(blackMove); // Применяем ход на доску
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Пропуск некорректной строки: " + line + " (" + e.getMessage() + ")");
                }
            }

            System.out.println("Партия успешно загружена из файла: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public void applyMove(Move move) {
//        if (move == null || move.movedPiece == null) {
//            throw new IllegalArgumentException("Некорректный ход: move или movedPiece равно null");
//        }
//
//        Piece piece = move.movedPiece; // Фигура, которая делает ход
//
//        if (!piece.isInBounds(move.toX, move.toY)) {
//            System.out.println("Некорректные координаты для хода: " + move.toX + "," + move.toY);
//            return;
//        }
//
//        move.capturedPiece = getPieceAt(move.toX, move.toY);
//
//        board[move.fromX][move.fromY] = null; // Очищаем исходную клетку
//        board[move.toX][move.toY] = piece; // Ставим фигуру на новую клетку
//
//        // Обновляем позицию фигуры
//        piece.setPosition(move.toX, move.toY);
//
//        // Проверка шахматной логики - превращение пешки в ферзя
//        if (piece instanceof Pawn) { // Если фигура - пешка
//            if ((piece.getColor().equals("white") && move.toX == 0) ||
//                    (piece.getColor().equals("black") && move.toX == 7)) {
//
//                board[move.toX][move.toY] = new Queen(piece.getColor(), move.toX, move.toY);
//                System.out.println("Пешка превращена в ферзя на позиции " + move.toX + "," + move.toY);
//            }
//        }
//
//        moveHistory.push(move);
//
//        currentPlayerColor = Objects.equals(piece.getColor(), "white") ? "black" : "white";
//
//        if (isKingInCheck(currentPlayerColor)) {
//            System.out.println("Шах королю " + this.currentPlayerColor + "от фигуры " + piece.pieceInfo());
//        }
//    }

    public void applyMove(Move move) {
        // Проверка на null
        if (move == null || move.movedPiece == null) {
            throw new IllegalArgumentException("Некорректный ход: move или movedPiece равно null");
        }

        // Проверка валидности координат
        if (!isValidCoordinate(move.fromX, move.fromY) || !isValidCoordinate(move.toX, move.toY)) {
            throw new IllegalArgumentException("Некорректные координаты: " +
                    "from(" + move.fromX + "," + move.fromY + "), " +
                    "to(" + move.toX + "," + move.toY + ")");
        }

        Piece piece = move.movedPiece;

        move.capturedPiece = getPieceAt(move.toX, move.toY); // Проверяем, есть ли фигура на целевой клетке

        // Выполняем перемещение
        board[move.fromX][move.fromY] = null; // Очищаем исходную клетку
        board[move.toX][move.toY] = piece; // Перемещаем фигуру

        // Обновляем положение фигуры
        piece.setPosition(move.toX, move.toY);

        // Логика превращения пешки
        if (piece instanceof Pawn) { // Если фигура является пешкой
            if ((piece.getColor().equals("white") && move.toX == 0) ||  // Белая пешка дошла до конца доски
                    (piece.getColor().equals("black") && move.toX == 7)) {  // Черная пешка дошла до конца доски

                board[move.toX][move.toY] = new Queen(piece.getColor(), move.toX, move.toY); // Превращаем в ферзя
                System.out.println("Пешка превращена в ферзя на позиции " + move.toX + "," + move.toY);
            }
        }

        // Фиксируем ход
        moveHistory.push(move);

        // Меняем текущего игрока
        currentPlayerColor = Objects.equals(piece.getColor(), "white") ? "black" : "white";

        // Проверяем шах
        if (isKingInCheck(currentPlayerColor)) {
            System.out.println("Шах королю " + this.currentPlayerColor + " от фигуры " + piece.pieceInfo());
        }
    }


    public List<Move> getAllValidMoves(String playerColor) {
        List<Move> moves = new ArrayList<>();

        // Итерируем по всем фигурам указанного цвета
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = getPieceAt(x, y);
                if (piece != null && piece.getColor().equals(playerColor)) {
                    // Добавляем все допустимые ходы для текущей фигуры
                    moves.addAll(getValidMoves(x, y));
                }
            }
        }

        return moves; // Возвращаем список всех валидных ходов
    }

    public Move parseNotationToMove(String notation) {
        if (notation == null || notation.isEmpty()) {
            throw new IllegalArgumentException("Нотация не может быть пустой или null");
        }

        // Убедимся, что длина нотации позволяет извлечь координаты (минимум 5 символов без учета взятий)
        if (notation.length() < 5) {
            throw new IllegalArgumentException("Некорректный формат нотации: " + notation);
        }

        try {
            // Определяем, является ли ход взятием (содержит 'x' или '-')
            char delimiter = notation.contains("x") ? 'x' : '-';

            // Разделяем строку по символу разделителя
            String[] parts = notation.split(Character.toString(delimiter));

            // Проверка на корректность разбиения
            if (parts.length != 2) {
                throw new IllegalArgumentException("Некорректный формат нотации: " + notation);
            }

            // Начальная клетка (2 символа в первой части)
            String from = parts[0];
            if (from.length() < 2) {
                throw new IllegalArgumentException("Некорректный формат начальной клетки: " + from);
            }
            int fromY = from.charAt(from.length() - 2) - 'a'; // 'a'-'h' -> 7-0
            int fromX = 8 - Character.getNumericValue(from.charAt(from.length() - 1)); // '1'-'8' -> 7-0

            // Конечная клетка (последние 2 символа второй части)
            String to = parts[1];
            if (to.length() < 2) {
                throw new IllegalArgumentException("Некорректный формат конечной клетки: " + to);
            }
            int toY = to.charAt(0) - 'a'; // 'a'-'h' -> 0-7
            int toX = 8 - Character.getNumericValue(to.charAt(1)); // '1'-'8' -> 7-0

            // Проверяем корректность индексации
            if (!isValidCoordinate(fromX, fromY) || !isValidCoordinate(toX, toY)) {
                throw new IllegalArgumentException("Некорректные координаты в нотации: " + notation);
            }

            // Извлекаем фигуры
            Piece movedPiece = getPieceAt(fromX, fromY);
            Piece capturedPiece = getPieceAt(toX, toY); // Может быть null, если нет взятия

            // Формируем и возвращаем объект Move
            return new Move(movedPiece, capturedPiece, fromX, fromY, toX, toY);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка обработки нотации: " + notation, e);
        }
    }


    // Дополнительный метод для проверки координат
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8; // Шахматная доска 8x8
    }


    public List<Move> getValidMoves(int x, int y) {
        List<Move> validMoves = new ArrayList<>();
        Piece piece = getPieceAt(x, y);

        if (piece == null || !Objects.equals(piece.getColor(), getCurrentPlayerColor())) {
            return validMoves; // Если нет фигуры или фигура не того игрока, возврат пустого списка
        }

        List<Move> potentialMoves = piece.getPotentialMoves(x, y, board);

        for (Move move : potentialMoves) {
            try {
                if (isMoveValid(move)) {
                    validMoves.add(move);
                }
            }
            catch (CloneNotSupportedException e) {
                System.out.println("NACHALNIKA VSE PLOHO - ONO NE KOPIRUETSA!!!");
            }
        }
        return validMoves;
    }

    private boolean isMoveValid(Move move) throws CloneNotSupportedException {

        // 1. Проверка базовой корректности
        Piece piece = getPieceAt(move.fromX, move.fromY);
        if (piece == null || !Objects.equals(piece.getColor(), getCurrentPlayerColor())) {
            return false;
        }

        // Проверяем, соответствует ли ход возможным ходам фигуры
        List<Move> potentialMoves = piece.getPotentialMoves(move.fromX, move.fromY, board);

        // System.out.println(potentialMoves);

        if (!potentialMoves.contains(move)) {
            return false;
        }

        // Проверяем, не приведет ли ход к шаху для игрока
        Board testBoard = (Board) this.clone(); // Копируем текущую доску для симуляции
        testBoard.applyMove(move); // Выполняем ход

        boolean isValid = !testBoard.isKingInCheck(piece.getColor());
        testBoard.undoMove();

        return isValid;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        Board clonedBoard = (Board) super.clone(); // Создаем поверхностную копию

        // Глубокое копирование массива доски
        clonedBoard.board = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.board[i][j] != null) {
                    clonedBoard.board[i][j] = (Piece) this.board[i][j].clone(); // Предполагается, что у Piece есть метод clone
                }
            }
        }

        return clonedBoard;
    }

    public String getCurrentPlayerColor() {
        return currentPlayerColor;
    }
}
