package model;

import java.util.List;

import model.Board.Move;
import model.Board;

//public class LongRangePiece extends Piece {
//    public LongRangePiece(String color, int x, int y) {
//        super(color, x, y);
//    }
//
////    public boolean isKingInCheck(String color) {
////        int kingX = -1, kingY = -1;
////
////        // Найдите короля
////        for (int i = 0; i < 8; i++) {
////            for (int j = 0; j < 8; j++) {
////                Piece piece = getPieceAt(i, j);
////                if (piece instanceof King && piece.getColor().equals(color)) {
////                    kingX = i;
////                    kingY = j;
////                    break;
////                }
////            }
////        }
////
////        // Проверьте, может ли любая фигура противника атаковать короля
////        for (int i = 0; i < 8; i++) {
////            for (int j = 0; j < 8; j++) {
////                Piece piece = getPieceAt(i, j);
////                if (piece != null && !piece.getColor().equals(color)) {
////                    if (piece.canAttack(kingX, kingY, this)) {
////                        System.out.println("Шах королю " + color + " от " + piece.pieceInfo());
////                        return true;
////                    }
////                }
////            }
////        }
////        return false;
////    }
//
//    // Check clear way for Bishop, Rook, Queen
//    public static boolean isPathClear(Board board, int fromX, int fromY, int toX, int toY) {
//            int deltaX = Integer.signum(toX - fromX);
//            int deltaY = Integer.signum(toY - fromY);
//            int x = fromX + deltaX, y = fromY + deltaY;
//
//            while (x != toX || y != toY) {
//                if (board.getPieceAt(x, y) != null) {
//                    return false;
//                }
//                x += deltaX;
//                y += deltaY;
//            }
//            return true;
//        }
//
//    @Override
//    public boolean isValidMove(int targetX, int targetY, Board board) {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'isValidMove'");
//    }
//
//    @Override
//    protected String getType() {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getType'");
//    }
//
//    @Override
//    public List<Move> getPotentialMoves(int x, int y, Piece[][] board) {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getPotentialMoves'");
//    }
//}
