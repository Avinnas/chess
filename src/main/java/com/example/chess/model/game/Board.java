package com.example.chess.model.game;

import com.example.chess.model.pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    HashMap<Integer, Piece> tilePieceAssignment;
   // # 0 - white,
    List<Map<String, List<Piece>>> piecesByType;


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
        HashMap<String, List<Piece>> whitePiecesByType = new HashMap<>();
        HashMap<String, List<Piece>> blackPiecesByType = new HashMap<>();

        for (Piece piece: tilePieceAssignment.values()) {
            String pieceClass = piece.getClass().getSimpleName();
            if (piece.getColor() == Color.WHITE){
                var existingPieces = whitePiecesByType.get(pieceClass);
                    if(existingPieces != null){
                        existingPieces.add(piece);
                        whitePiecesByType.put(pieceClass, existingPieces);
                    }
                    else
                    {
                        var temp = new ArrayList<Piece>();
                        temp.add(piece);
                        whitePiecesByType.put(pieceClass, temp);
                    }
            }
            else
            {
                var existingPieces = blackPiecesByType.get(pieceClass);
                if(existingPieces != null){
                    existingPieces.add(piece);
                    blackPiecesByType.put(pieceClass, existingPieces);
                }
                else
                {
                    var temp = new ArrayList<Piece>();
                    temp.add(piece);
                    blackPiecesByType.put(pieceClass, temp);
                }
            }
        }
        piecesByType = new ArrayList<>();

        piecesByType.add(whitePiecesByType);
        piecesByType.add(blackPiecesByType);
    }
    public HashMap<Integer, List<Integer>> findCurrentPlayerMoves(Color currentColor){
        HashMap<Integer, List<Integer>> possibleMoves = new HashMap<>();
        for (List<Piece> piecesTypeList : piecesByType.get(currentColor.getValue()).values()){
            for (Piece piece: piecesTypeList) {

                List<Integer> tiles = piece.findPossibleMoves(this);
                if(!tiles.isEmpty()){
                    possibleMoves.put(piece.getTileNumber(), tiles);
                }

            }
        }

        return possibleMoves;
    }

    public void removePieceAfterCapturing(Piece pieceToRemove){


        piecesByType
                .get(pieceToRemove.getColor().getValue())
                .get(pieceToRemove.getName())
                .remove(pieceToRemove);

        System.out.println(piecesByType.get(pieceToRemove.getColor().getValue()));

    }
    public String calculateHash(Move move, boolean isCapture){
        String hash = "";
        Piece piece = this.tilePieceAssignment.get(move.getStartTile());
        if (!piece.getName().equals("Pawn")) {
            if(piece.getName().equals("Knight")){
                hash += 'N';
            }
            else{
                hash += piece.getName().charAt(0);
            }
        }
        if(isCapture){
            hash += 'x';
        }
        hash += tileHash(move.getDestinationTile());
        return hash;
    }

    public String tileHash(int tileId){
        int row = 8-(tileId / 8) ;
        String column = String.valueOf((char)((tileId % 8) +97));

        return column + row;
    }

    public Move makeMove(int actualTileId, int destinationTileId){

        Move moveMade = new Move(actualTileId,destinationTileId);

        if(tilePieceAssignment.containsKey(destinationTileId)){
            removePieceAfterCapturing(tilePieceAssignment.get(destinationTileId));
            moveMade.setHash(calculateHash(moveMade, true));
        }
        else{
            moveMade.setHash(calculateHash(moveMade, false));
        }
        Piece piece = tilePieceAssignment.remove(actualTileId);


        List<Piece> pieceList = piecesByType.get(piece.getColor().getValue()).get(piece.getName());
        pieceList.get(pieceList.indexOf(piece)).setTileNumber(destinationTileId);

        tilePieceAssignment.put(destinationTileId, piece);

        return moveMade;
    }

    public Move makeMove(Move moveToMake){
        return makeMove(moveToMake.getStartTile(), moveToMake.getDestinationTile());
    }

    public HashMap<Integer, Piece> getTilePieceAssignment() {
        return tilePieceAssignment;
    }
    public Map<String, List<Piece>> getPlayerPieces(Color color){
        return piecesByType.get(color.getValue());
    }

    public List<Map<String, List<Piece>>> getPiecesByType() {
        return piecesByType;
    }

    public boolean tileIsEmpty(int tileNumber){
        return !tilePieceAssignment.containsKey(tileNumber);
    }
    public boolean tileIsOccupiedByOpponent(int tileNumber, Color pieceColor){
        return !tileIsEmpty(tileNumber) && tilePieceAssignment.get(tileNumber).getColor()!=pieceColor;
    }


    public void calculateState(List<Move> moves) {
        moves.forEach(this::makeMove);
    }
}
