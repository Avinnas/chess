package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.pieces.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    HashMap<Integer, Piece> tilePieceAssignment;


    public Board() {
        tilePieceAssignment = new HashMap<>();

        tilePieceAssignment.put(0, new Rook(0, Color.BLACK));
        tilePieceAssignment.put(1, new Knight(1, Color.BLACK));
        tilePieceAssignment.put(2, new Bishop(2, Color.BLACK));
        tilePieceAssignment.put(3, new Queen(3, Color.BLACK));
        tilePieceAssignment.put(4, new King(4, Color.BLACK));
        tilePieceAssignment.put(5, new Bishop(5, Color.BLACK));
        tilePieceAssignment.put(6, new Knight(6, Color.BLACK));
        tilePieceAssignment.put(7, new Rook(7, Color.BLACK));

        tilePieceAssignment.put(8, new Pawn(8, Color.BLACK));
        tilePieceAssignment.put(9, new Pawn(9, Color.BLACK));
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

        tilePieceAssignment.put(56, new Rook(56, Color.WHITE));
        tilePieceAssignment.put(57, new Knight(57, Color.WHITE));
        tilePieceAssignment.put(58, new Bishop(58, Color.WHITE));
        tilePieceAssignment.put(59, new Queen(59, Color.WHITE));
        tilePieceAssignment.put(60, new King(60, Color.WHITE));
        tilePieceAssignment.put(61, new Bishop(61, Color.WHITE));
        tilePieceAssignment.put(62, new Knight(62, Color.WHITE));
        tilePieceAssignment.put(63, new Rook(63, Color.WHITE));



    }

    public Board(Piece[] pieces) {
        tilePieceAssignment = new HashMap<>();
        for (Piece piece : pieces) {
            tilePieceAssignment.put(piece.getTileNumber(), piece);
        }


    }

    public Board(Board board) {
        tilePieceAssignment = new HashMap<>(board.getTilePieceAssignment());

    }

    public HashMap<Integer, List<Integer>> findCurrentPlayerMoves(Color currentColor) {
        HashMap<Integer, List<Integer>> possibleMoves = new HashMap<>();
        for (Piece piece : tilePieceAssignment.values()) {
            if (piece.getColor() == currentColor) {
                List<Integer> tiles = piece.findPossibleMoves(this);
                if (!tiles.isEmpty()) {
                    possibleMoves.put(piece.getTileNumber(), tiles);
                }
            }

        }

        return possibleMoves;
    }


    public void makeMove(MoveDto moveToMake) {
        makeMove(MoveFactory.getMove(moveToMake, this));
    }

    public void makeCastlingMove(CastlingMove moveToMake) {
        makeDefaultMove(moveToMake);
        makeMove(new Move(moveToMake.getRookStartTile(), moveToMake.getRookDestinationTile()));
    }

    public void makePromotionMove(PromotionMove moveToMake) {
        makeDefaultMove(moveToMake);
        Piece p = tilePieceAssignment.remove(moveToMake.getDestinationTile());
        tilePieceAssignment.put(p.getTileNumber(), new Queen(p.getTileNumber(), p.getColor()));

    }


    public void makeMove(Move moveToMake) {
        if (moveToMake.getClass().getSimpleName().equals("CastlingMove"))
            makeCastlingMove((CastlingMove) moveToMake);
        else if (moveToMake.getClass().getSimpleName().equals("PromotionMove"))
            makePromotionMove((PromotionMove) moveToMake);
        else
            makeDefaultMove(moveToMake);

    }

    public void makeDefaultMove(Move moveToMake) {
        int destinationTileId = moveToMake.getDestinationTile();
        int actualTileId = moveToMake.getStartTile();

        Piece piece = tilePieceAssignment.remove(actualTileId);

        Piece clonedPiece = piece.clone();
        clonedPiece.setTileNumber(destinationTileId);

        tilePieceAssignment.put(destinationTileId, clonedPiece);
    }

    public void calculateState(List<Move> moves) {
        moves.forEach(this::makeMove);
    }

    public HashMap<Integer, Piece> getTilePieceAssignment() {
        return tilePieceAssignment;
    }

    public Piece getPiece(int tileNumber) {
        return tilePieceAssignment.get(tileNumber);
    }

    public void removePiece(int tileNumber) {
        Piece p = tilePieceAssignment.remove(tileNumber);
    }

    public List<Piece> getPlayerPieces(Color color) {
        return tilePieceAssignment.values().stream()
                .filter(x -> x.getColor()==color)
                .collect(Collectors.toList());
    }


    public boolean tileIsEmpty(int tileNumber) {
        return !tilePieceAssignment.containsKey(tileNumber);
    }

    public boolean tileIsOccupiedByOpponent(int tileNumber, Color pieceColor) {
        return !tileIsEmpty(tileNumber) && tilePieceAssignment.get(tileNumber).getColor() != pieceColor;
    }


}
