package com.example.chess.model.dto;

import com.example.chess.model.game.Move;

public class MoveDto {
    private final int startTile;
    private final int destinationTile;

    public MoveDto(int startTile, int destinationTile) {
        this.startTile = startTile;
        this.destinationTile = destinationTile;
    }

    public MoveDto(Move m){
        this.startTile = m.getStartTile();
        this.destinationTile = m.getDestinationTile();
    }

    public MoveDto() {
        startTile = -1;
        destinationTile = -1;
    }
    public String toString(){
        return "\n" + startTile + " --> " + destinationTile;
    }

    public int getStartTile() {
        return startTile;
    }

    public int getDestinationTile() {
        return destinationTile;
    }

}
