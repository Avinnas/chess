package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("King")
public class King extends Piece{
    boolean castleAllowed;

    public King(int tileNumber, Color color) {
        super(tileNumber, color);
        this.castleAllowed = true;
    }

    @Override
    public List<Integer> findPossibleMoves(Board board) {
        return null;
    }
}
