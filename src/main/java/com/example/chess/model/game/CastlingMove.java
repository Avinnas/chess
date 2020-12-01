package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class CastlingMove extends Move {

    @Transient
    int rookStartTile;
    @Transient
    int rookDestinationTile;

    public CastlingMove(MoveDto move){
        super(move);
        calculateRookPosition(isLongCastle());

    }

    public CastlingMove() {

    }

    public boolean isLongCastle(){
        return destinationTile == 58 || destinationTile == 2;
    }
    public void calculateRookPosition(boolean longCastle){
        int distance = longCastle ? 2 : 1;
        int direction = (destinationTile - startTile)/2;

        this.rookStartTile = destinationTile + direction * distance;
        this.rookDestinationTile = destinationTile - direction;
    }

    @Override
    public void calculateHash(Board board){
        if(isLongCastle()){
            setHash("O-O-O");
        }
        else{
            setHash("O-O");
        }
    }


    public int getRookStartTile() {
        return rookStartTile;
    }

    public void setRookStartTile(int rookStartTile) {
        this.rookStartTile = rookStartTile;
    }

    public int getRookDestinationTile() {
        return rookDestinationTile;
    }

    public void setRookDestinationTile(int rookDestinationTile) {
        this.rookDestinationTile = rookDestinationTile;
    }
}
