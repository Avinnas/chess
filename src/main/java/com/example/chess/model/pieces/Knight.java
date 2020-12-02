package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.*;

@JsonTypeName("Knight")
public class Knight extends Piece {

    public Knight(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    @Override
    public HashSet<Integer> findPossibleMoves(Board board){
        return findPossibleMoves(board, false);
    }

    public HashSet<Integer> findPossibleMoves(Board board, boolean includeControlledTiles) {
        List <Integer> relativePossibleMoves = new LinkedList<>(Arrays.asList(-17, -15, -10, -6, 6, 10, 15, 17));
        HashSet<Integer> moves = new HashSet<>();
        int column = tileNumber % 8;
        int row = tileNumber / 8;

        if(column < 2){
            relativePossibleMoves.remove(Integer.valueOf(-10));
            relativePossibleMoves.remove(Integer.valueOf(6));
            if(column == 0){
                relativePossibleMoves.remove(Integer.valueOf(-17));
                relativePossibleMoves.remove(Integer.valueOf(15));
            }
        }
        if(column>5){
            relativePossibleMoves.remove(Integer.valueOf(10));
            relativePossibleMoves.remove(Integer.valueOf(-6));
            if(column==7){
                relativePossibleMoves.remove(Integer.valueOf(17));
                relativePossibleMoves.remove(Integer.valueOf(-15));
            }
        }
        for (int tile : relativePossibleMoves) {
            int realTileNumber = tile + this.tileNumber;
            if (realTileNumber < 64 && realTileNumber >= 0 &&
                    (board.tileIsEmpty(realTileNumber)
                            || board.tileIsOccupiedByOpponent(realTileNumber, this.color)
                    || includeControlledTiles)) {
                moves.add(realTileNumber);
            }
        }
        return moves;

    }

}
