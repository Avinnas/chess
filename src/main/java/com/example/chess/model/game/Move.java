package com.example.chess.model.game;

import com.example.chess.model.pieces.Piece;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Move {

    @Id
    int id;

    int startTile;
    int destinationTile;
    String pieceType;
    Color pieceColor;

    public String toString(){
        return "\n" + startTile + " --> " + destinationTile + " " + pieceColor + " " + pieceType;
    }
    public Move(){

    }

    public Move(int destinationTile, Piece piece){
        this.startTile = piece.getTileNumber();
        this.destinationTile = destinationTile;
        this.pieceType = piece.getClass().getSimpleName();
        this.pieceColor = piece.getColor();

    }

    public int getStartTile() {
        return startTile;
    }

    public int getDestinationTile() {
        return destinationTile;
    }

    public String getPieceType() {
        return pieceType;
    }

    public Color getPieceColor() {
        return pieceColor;
    }
}
