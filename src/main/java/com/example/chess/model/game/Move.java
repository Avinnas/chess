package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.pieces.Piece;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "moves")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;

    int startTile;
    int destinationTile;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonBackReference
    Game game;

    String hash;


    public String toString(){
        return "\n" + startTile + " --> " + destinationTile;
    }
    public Move(){

    }


    public void setGame(Game game) {
        this.game = game;
    }

    public Move(int startTile, int destinationTile) {
        this.startTile = startTile;
        this.destinationTile = destinationTile;
    }

    public Move(int destinationTile, Piece piece){
        this(piece.getTileNumber(), destinationTile);
    }

    public Move(MoveDto move){
        this.startTile = move.getStartTile();
        this.destinationTile = move.getDestinationTile();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        Move move = (Move) obj;
        return this.startTile == move.startTile
                && this.destinationTile == move.destinationTile
                && this.hash.equals(move.hash);
    }

    public boolean isCapture(Board board) {
        return board.getTilePieceAssignment().containsKey(destinationTile);
    }
    public void calculateHash(Board board) {
        String temp = "";
        Piece piece = board.getPiece(startTile);
        if (!piece.getName().equals("Pawn")) {
            if (piece.getName().equals("Knight")) {
                temp += 'N';
            } else {
                temp += piece.getName().charAt(0);
            }
        }

        if (isCapture(board)) {
            temp += 'x';
        }
        temp += tileHash(destinationTile);
        hash = temp;
    }
    public String tileHash(int tileId) {
        int row = 8 - (tileId / 8);
        String column = String.valueOf((char) ((tileId % 8) + 97));

        return column + row;
    }


    public int getStartTile() {
        return startTile;
    }

    public int getDestinationTile() {
        return destinationTile;
    }

    public Game getGame() {
        return game;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStartTile(int startTile) {
        this.startTile = startTile;
    }

    public void setDestinationTile(int destinationTile) {
        this.destinationTile = destinationTile;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
