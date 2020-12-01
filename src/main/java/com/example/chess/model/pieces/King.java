package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@JsonTypeName("King")
public class King extends Piece{
    public King(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    public List<Integer> findCastlingMoves(Board board){

        return new ArrayList<>();
    }

    @Override
    public List<Integer> findPossibleMoves(Board board) {
        List <Integer> relativePossibleMoves = new LinkedList<>(Arrays.asList(-7, -8, -9, -1, 1, 7, 8,9));
        List <Integer> possibleMoves = new ArrayList<>();
        int column = this.tileNumber % 8;
        if(column == 0){
            relativePossibleMoves.remove(Integer.valueOf(-9));
            relativePossibleMoves.remove(Integer.valueOf(-1));
            relativePossibleMoves.remove(Integer.valueOf(7));
        }
        if(column == 7){
            relativePossibleMoves.remove(Integer.valueOf(9));
            relativePossibleMoves.remove(Integer.valueOf(1));
            relativePossibleMoves.remove(Integer.valueOf(-7));
        }

        for (int tile: relativePossibleMoves){
            int realTileNumber = tile + this.tileNumber;
            if( realTileNumber <64 && realTileNumber >=0 &&
                    (board.tileIsEmpty(realTileNumber) || board.tileIsOccupiedByOpponent(realTileNumber, this.color)) ){
                possibleMoves.add(realTileNumber);
            }
        }


        return possibleMoves;
    }
}
