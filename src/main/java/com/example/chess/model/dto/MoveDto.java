package com.example.chess.model.dto;

import com.example.chess.model.game.Move;

public class MoveDto {
    private final int actualTileId;
    private final int destinationId;

    public MoveDto(int actualTileId, int destinationId) {
        this.actualTileId = actualTileId;
        this.destinationId = destinationId;
    }

    public MoveDto(Move m ){
        this.actualTileId = m.getStartTile();
        this.destinationId = m.getDestinationTile();
    }

    public int getActualTileId() {
        return actualTileId;
    }

    public int getDestinationId() {
        return destinationId;
    }

}
