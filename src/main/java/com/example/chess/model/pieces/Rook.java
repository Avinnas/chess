package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("Rook")
public class Rook extends Piece {
    public Rook(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    @Override
    public List<Integer> findPossibleMoves(Board board) {
        List<Integer> possibleMoves = new ArrayList<>();

        possibleMoves.addAll(this.searchForMovesInDirection(1,0, board));
        possibleMoves.addAll(this.searchForMovesInDirection(-1,0, board));
        possibleMoves.addAll(this.searchForMovesInDirection(0,1, board));
        possibleMoves.addAll(this.searchForMovesInDirection(0,-1, board));

        return possibleMoves;
    }

}
