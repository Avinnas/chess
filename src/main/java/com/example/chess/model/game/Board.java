package com.example.chess.model.game;

import com.example.chess.model.pieces.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    HashMap<Integer, Piece> tilePieceAssignment;
   // # 0 - white,
    List<Map<String, Piece[]>> piecesByType;


    public Board() {
        tilePieceAssignment = new HashMap<>();
        
        tilePieceAssignment.put(0, new Rook(  0, Color.BLACK));
        tilePieceAssignment.put(1, new Knight(1, Color.BLACK));
        tilePieceAssignment.put(2, new Bishop(2, Color.BLACK));
        tilePieceAssignment.put(3, new Queen( 3, Color.BLACK));
        tilePieceAssignment.put(4, new King(  4, Color.BLACK));
        tilePieceAssignment.put(5, new Bishop(5, Color.BLACK));
        tilePieceAssignment.put(6, new Knight(6, Color.BLACK));
        tilePieceAssignment.put(7, new Rook(  7, Color.BLACK));

        tilePieceAssignment.put( 8, new Pawn( 8, Color.BLACK));
        tilePieceAssignment.put( 9, new Pawn( 9, Color.BLACK));
        tilePieceAssignment.put(10, new Pawn(10, Color.BLACK));
        tilePieceAssignment.put(11, new Pawn(11, Color.BLACK));
        tilePieceAssignment.put(12, new Pawn(12, Color.BLACK));
        tilePieceAssignment.put(13, new Pawn(13, Color.BLACK));
        tilePieceAssignment.put(14, new Pawn(14, Color.BLACK));
        tilePieceAssignment.put(15, new Pawn(15, Color.BLACK));

        tilePieceAssignment.put(48, new Pawn(48, Color.WHITE));
        tilePieceAssignment.put(49, new Pawn(49, Color.WHITE));
        tilePieceAssignment.put(50, new Pawn(50, Color.WHITE));
        tilePieceAssignment.put(51, new Pawn(51, Color.WHITE));
        tilePieceAssignment.put(52, new Pawn(52, Color.WHITE));
        tilePieceAssignment.put(53, new Pawn(53, Color.WHITE));
        tilePieceAssignment.put(54, new Pawn(54, Color.WHITE));
        tilePieceAssignment.put(55, new Pawn(55, Color.WHITE));

        tilePieceAssignment.put(56, new Rook(  56, Color.WHITE));
        tilePieceAssignment.put(57, new Knight(57, Color.WHITE));
        tilePieceAssignment.put(58, new Bishop(58, Color.WHITE));
        tilePieceAssignment.put(59, new Queen( 59, Color.WHITE));
        tilePieceAssignment.put(60, new King(  60, Color.WHITE));
        tilePieceAssignment.put(61, new Bishop(61, Color.WHITE));
        tilePieceAssignment.put(62, new Knight(62, Color.WHITE));
        tilePieceAssignment.put(63, new Rook(  63, Color.WHITE));

        fillPiecesByType();

    }

    public Board(Piece[] pieces){
        tilePieceAssignment = new HashMap<>();
        for (Piece piece : pieces){
            tilePieceAssignment.put(piece.getTileNumber(), piece);
        }

        fillPiecesByType();
    }
    private void fillPiecesByType(){
//        HashMap<String, Piece> whitePiecesByType = new HashMap<>();
//        HashMap<String, Piece> blackPiecesByType = new HashMap<>();
//        for (Piece piece: tilePieceAssignment.values()) {
//            if (piece.getColor() == Color.WHITE){
//                whitePiecesByType.
//                whitePiecesByType.put(piece.getClass().getSimpleName(), piece);
//            }
//            else
//                blackPiecesByType.put(piece.getClass().getSimpleName(), piece);
//        }
//        piecesByType = new ArrayList<>();
//
//        // TODO -> KAŻDY KLUCZ POWINIEN PRZYJMOWAć LISTE BIEREK
//        piecesByType.add(whitePiecesByType);
//        piecesByType.add(blackPiecesByType);
    }
    public HashMap<?,?> findAllPossibleMoves(){
        HashMap<Integer, List<Integer>> possibleMoves = new HashMap<>();
        for (Piece piece: tilePieceAssignment.values()) {

            List<Integer> tiles = piece.findPossibleMoves(this);
            for(Integer tile : tiles){
                possibleMoves.put(piece.getTileNumber(), tiles);
            }
        }
        return possibleMoves;
    }

    public HashMap<Integer, Piece> getTilePieceAssignment() {
        return tilePieceAssignment;
    }

    public List<Map<String, Piece[]>> getPiecesByType() {
        return piecesByType;
    }

    public boolean tileIsEmpty(int tileNumber){
        return !tilePieceAssignment.containsKey(tileNumber);
    }
    public boolean tileIsOccupiedByOpponent(int tileNumber, Color pieceColor){
        return !tileIsEmpty(tileNumber) && tilePieceAssignment.get(tileNumber).getColor()!=pieceColor;
    }

}
