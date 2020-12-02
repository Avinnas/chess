package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.HashSet;

@JsonTypeName("Queen")
public class Queen extends Piece {
    public Queen(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    @Override
    public HashSet<Integer> findPossibleMoves(Board board) {
        return  findPossibleMoves(board, false);
    }
    public HashSet<Integer> findPossibleMoves(Board board, boolean includeControlledTiles){
        HashSet<Integer> possibleMoves = new HashSet<>();

        possibleMoves.addAll(this.searchForMovesInDirection(-1,-1, board, includeControlledTiles));
        possibleMoves.addAll(this.searchForMovesInDirection(-1,0, board, includeControlledTiles));
        possibleMoves.addAll(this.searchForMovesInDirection(-1,1, board, includeControlledTiles));


        possibleMoves.addAll(this.searchForMovesInDirection(0,-1, board, includeControlledTiles));
        possibleMoves.addAll(this.searchForMovesInDirection(0,1, board, includeControlledTiles));

        possibleMoves.addAll(this.searchForMovesInDirection(1,-1, board, includeControlledTiles));
        possibleMoves.addAll(this.searchForMovesInDirection(1,0, board, includeControlledTiles));
        possibleMoves.addAll(this.searchForMovesInDirection(1,1, board, includeControlledTiles));

        return possibleMoves;
    }

}
