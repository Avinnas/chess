package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.HashSet;

@JsonTypeName("Bishop")
public class Bishop extends Piece {
    public Bishop(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    @Override
    public HashSet<Integer> findPossibleMoves(Board board) {
        return  findPossibleMoves(board, false);
    }
    public HashSet<Integer> findPossibleMoves(Board board, boolean includeControlledTiles){
        HashSet<Integer> possibleMoves = new HashSet<>();

        possibleMoves.addAll(this.searchForMovesInDirection(-1,-1, board, includeControlledTiles));
        possibleMoves.addAll(this.searchForMovesInDirection(-1,1, board, includeControlledTiles));
        possibleMoves.addAll(this.searchForMovesInDirection(1,-1, board, includeControlledTiles));
        possibleMoves.addAll(this.searchForMovesInDirection(1,1, board, includeControlledTiles));

        return possibleMoves;
    }



}
