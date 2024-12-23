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
    

    public Piece getPieceAt(int x, int y) {
        return board[x][y];
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
    

    // public boolean movePiece(int fromX, int fromY, int toX, int toY) {
    //     Piece piece = getPieceAt(fromX, fromY);
    //     if (piece != null && piece.isValidMove(toX, toY, this)) {
    //         Piece captured = board[toX][toY];
    //         moveHistory.push(new Move(piece, captured, fromX, fromY, toX, toY));
    //         board[toX][toY] = piece;
    //         board[fromX][fromY] = null;
    //         currentPlayerColor = piece.getColor() == "white" ? "black" : "white"; 
    //         return true;
    //     }
    //     return false;
    // }
    
    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            board[lastMove.fromX][lastMove.fromY] = lastMove.movedPiece;
            board[lastMove.toX][lastMove.toY] = lastMove.capturedPiece;
            currentPlayerColor = lastMove.movedPiece.getColor();
//            currentPlayerColor = lastMove.movedPiece.getColor() == "white" ? "black" : "white";
        }
    }    

    // // Check clear way for Bishop, Rook, Queen
    // public boolean isPathClear(int fromX, int fromY, int toX, int toY) {
    //     int deltaX = Integer.signum(toX - fromX);
    //     int deltaY = Integer.signum(toY - fromY);
    //     int x = fromX + deltaX, y = fromY + deltaY;
    
    //     while (x != toX || y != toY) {
    //         if (board[x][y] != null) {
    //             return false;
    //         }
    //         x += deltaX;
    //         y += deltaY;
    //     }
    //     return true;
    // }

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

//    public boolean noValidMoves(String color) {
//        // Проходим по всем фигурам игрока
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                List<Move> validMoves = getValidMoves(i, j);
//
//                if (validMoves.isEmpty()) {
//                    return false;
//                }
//            }
//        }
//        return true; // Нет допустимых ходов
//    }

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
        setupInitialPosition(); // Сбрасываем доску в начальное состояние

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Разделяем номер хода, ход белых и, возможно, ход чёрных

                if (parts.length >= 2) {
                    Move whiteMove = parseNotationToMove(parts[1]);
                    applyMove(whiteMove);
                }

                if (parts.length == 3) {
                    Move blackMove = parseNotationToMove(parts[2]);
                    applyMove(blackMove);
                }
            }
            System.out.println("Партия успешно загружена из файла: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isCheckmate(String color) {
        // Проверить, находится ли король в шахе
        if (!isKingInCheck(color)) {
            return false; // Если король не в шахе, мата нет
        }

        // Проверить все возможные ходы каждой фигуры данного цвета
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = getPieceAt(x, y);
                if (piece != null && piece.getColor().equals(color)) {
                    // Попробовать все возможные ходы фигуры
                    for (int newX = 0; newX < 8; newX++) {
                        for (int newY = 0; newY < 8; newY++) {
                            if (piece.isValidMove(newX, newY, this)) {

                                Move tempMove = new Move(piece, getPieceAt(newX, newY), x, y, newX, newY);
                                // Сделать временный ход
                                applyMove(tempMove);

                                // Проверить, осталось ли состояние шаха после хода
                                boolean stillInCheck = isKingInCheck(color);

                                // Откат хода
                                undoMove();

                                // Если есть хотя бы один ход, который выводит короля из шаха, мата нет
                                if (!stillInCheck) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        // Если ни один ход не выводит короля из шаха, это мат
        return true;
    }

    public void applyMove(Move move) {
        Piece piece = move.movedPiece; // Фигура, которая делает ход
        // Если была съедена фигура, добавьте дополнительную логику, например:
        move.capturedPiece = getPieceAt(move.toX, move.toY);

        board[move.fromX][move.fromY] = null; // Очищаем исходную клетку
        board[move.toX][move.toY] = piece; // Ставим фигуру на новую клетку

        // Обновляем позицию фигуры
        piece.setPosition(move.toX, move.toY);


        // Сохраняем ход в историю
        moveHistory.push(move);

        currentPlayerColor = Objects.equals(piece.getColor(), "white") ? "black" : "white";

        if (isKingInCheck(currentPlayerColor)) {
            System.out.println("Шах королю " + this.currentPlayerColor + "от фигуры " + piece.pieceInfo());
        }
    }


    private Move parseNotationToMove(String notation) {
        String fromNotation = notation.substring(notation.indexOf("-")-2, notation.indexOf("-")); // Исходная клетка: "e2"
        String toNotation = notation.substring(notation.length() - 2); // Целевая клетка: "e4"

        int fromX = 8 - Character.getNumericValue(fromNotation.charAt(1));
        int fromY = fromNotation.charAt(0) - 'a';

        int toX = 8 - Character.getNumericValue(toNotation.charAt(1));
        int toY = toNotation.charAt(0) - 'a';

        Piece piece = getPieceAt(fromX, fromY);
        return new Move(piece, getPieceAt(toX, toY), fromX, fromY, toX, toY);
    }


    public List<Move> getValidMoves(int x, int y) {
        List<Move> validMoves = new ArrayList<>();
        Piece piece = getPieceAt(x, y);

        if (piece == null || piece.getColor() != getCurrentPlayerColor()) {
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
        // if (move.movedPiece.getColor() != getCurrentPlayerColor()) {
        //     return false;
        // }

        // 1. Проверка базовой корректности
        Piece piece = getPieceAt(move.fromX, move.fromY);
        if (piece == null || !Objects.equals(piece.getColor(), getCurrentPlayerColor())) {
            return false;
        }

        // Проверяем, соответствует ли ход возможным ходам фигуры
        List<Move> potentialMoves = piece.getPotentialMoves(move.fromX, move.fromY, board);

        System.out.println(potentialMoves);

        if (!potentialMoves.contains(move)) {
            return false;
        }

        // Проверяем, не приведет ли ход к шаху для игрока
        Board testBoard = (Board) this.clone(); // Копируем текущую доску для симуляции
        testBoard.applyMove(move); // Выполняем ход

        return !testBoard.isKingInCheck(piece.getColor());
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

        // // Клонируем историю ходов
        // if (this.moveHistory != null) {
        //     clonedBoard.moveHistory.addAll(this.moveHistory);
        // }

        return clonedBoard;
    }

    public String getCurrentPlayerColor() {
        return currentPlayerColor;
    }
}
