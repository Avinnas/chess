package com.example.chess.model.game;

public class CastlingMove extends Move {
    boolean isLongCastle;
    int rookStartTile;
    int rookDestinationTile;

    public CastlingMove(boolean isLongCastle) {
        this.isLongCastle = isLongCastle;
    }

    public void calculateRookPosition(boolean isLongCastle){
        this.isLongCastle = isLongCastle;
        int distance = isLongCastle ? 2 : 1;
        int direction = (destinationTile - startTile)/2;

        this.rookStartTile = destinationTile + direction * distance;
        this.rookDestinationTile = destinationTile - direction;
    }


    public boolean isLongCastle() {
        return isLongCastle;
    }

    public void setLongCastle(boolean longCastle) {
        isLongCastle = longCastle;
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
