package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.pieces.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    HashMap<Integer, Piece> tilePieceAssignment;
    boolean[] castlingRights;


    public Board() {

        // Castling
        // 0 - whiteShort
        // 1 - whiteLong
        // 2 - blackShort
        // 3 - blackLong
        castlingRights = new boolean[]{true, true, true, true};

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
        castlingRights = board.getCastlingRights().clone();
    }

    public HashMap<Integer, HashSet<Integer>> findCurrentPlayerMoves(Color currentColor) {
        HashMap<Integer, HashSet<Integer>> possibleMoves = new HashMap<>();
        for (Piece piece : getPlayerPieces(currentColor)) {
            HashSet<Integer> tiles = piece.findPossibleMoves(this);
            if (!tiles.isEmpty()) {
                possibleMoves.put(piece.getTileNumber(), tiles);
            }
        }

        return possibleMoves;
    }

    public HashMap<Integer, HashSet<Integer>> findTilesControlled(Color currentColor) {
        HashMap<Integer,HashSet<Integer>> controlledTiles = new HashMap<>();

        for (Piece piece : getPlayerPieces(currentColor)) {
            HashSet<Integer> tiles = piece.findPossibleMoves(this, true);
            if (!tiles.isEmpty()) {
                controlledTiles.put(piece.getTileNumber(), tiles);
            }
        }
        return controlledTiles;
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
        switch(moveToMake.getClass().getSimpleName()) {
            case "CastlingMove":
                makeCastlingMove((CastlingMove) moveToMake);
                break;
            case "PromotionMove":
                makePromotionMove((PromotionMove) moveToMake);
                break;
            default:
                makeDefaultMove(moveToMake);
        }
    }

    public void makeDefaultMove(Move moveToMake) {

        checkCastlingRights(moveToMake);

        int destinationTileId = moveToMake.getDestinationTile();
        int actualTileId = moveToMake.getStartTile();

        Piece piece = tilePieceAssignment.remove(actualTileId);

        Piece clonedPiece = piece.clone();
        clonedPiece.setTileNumber(destinationTileId);

        tilePieceAssignment.put(destinationTileId, clonedPiece);
    }
    public void checkCastlingRights(Move moveToMake){
        Piece p = getPiece(moveToMake.getStartTile());
        if(p.getName().equals("King")){
            disallowCastling(p.getColor());
        }
        if(p.getName().equals("Rook")){
            if(moveToMake.getStartTile() == 0 || moveToMake.getStartTile() == 56)
                disallowLongCastling(p.getColor());

            if(moveToMake.getStartTile() == 7 || moveToMake.getStartTile() == 63)
                disallowShortCastling(p.getColor());
        }
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
                .filter(x -> x.getColor() == color)
                .collect(Collectors.toList());
    }


    public boolean tileIsEmpty(int tileNumber) {
        return !tilePieceAssignment.containsKey(tileNumber);
    }

    public boolean tileIsOccupiedByOpponent(int tileNumber, Color pieceColor) {
        return !tileIsEmpty(tileNumber) && tilePieceAssignment.get(tileNumber).getColor() != pieceColor;
    }

    public boolean[] getCastlingRights() {
        return castlingRights;
    }

    public boolean shortCastlingAllowed(Color color) {
        return Color.WHITE == color ? castlingRights[0] : castlingRights[2];
    }

    public boolean longCastlingAllowed(Color color) {
        return Color.WHITE == color ? castlingRights[1] : castlingRights[3];
    }

    public void disallowCastling(Color color) {
        if (Color.WHITE == color) {
            castlingRights[0] = false;
            castlingRights[1] = false;
        } else {
            castlingRights[2] = false;
            castlingRights[3] = false;
        }

    }

    public void disallowLongCastling(Color color) {
        if (Color.WHITE == color) {
            castlingRights[1] = false;
        } else {
            castlingRights[3] = false;
        }
    }

    public void disallowShortCastling(Color color) {
        if (Color.WHITE == color) {
            castlingRights[0] = false;
        } else {
            castlingRights[2] = false;
        }
    }
    public HashSet<Integer> findCheckingPieces(int kingTile, Color kingColor){
        HashSet<Integer> checkingPieces = new HashSet<>();
        var tilesControlled = findTilesControlled(kingColor.getOpponentColor());
        for(Integer pieceTile: tilesControlled.keySet()){
            if(tilesControlled.get(pieceTile).contains(kingTile)){
                checkingPieces.add(pieceTile);
            }

        }
        return checkingPieces;
    }

    public int getKingTile(Color color){
        for(Piece piece: tilePieceAssignment.values()){
            if(piece.getName().equals("King") && piece.getColor() == color)
                return piece.getTileNumber();
        }
        throw new IllegalStateException("No king on board");
    }
}
