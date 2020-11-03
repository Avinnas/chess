package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonTypeName("Pawn")
public class Pawn extends Piece {
    public Pawn(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    @Override
    public List<Integer> findPossibleMoves(Board board) {
        List<Integer> possibleMoves = new ArrayList<>();
//        int row = tileNumber / 8;
//        if (this.color == game.Color.WHITE) {
//            if (row == 6) {
//                if (board.getTile(tileNumber - 16).isEmpty()
//                        && board.getTile(tileNumber - 8).isEmpty()) {
//                    possibleMoves.add(tileNumber - 16);
//                }
//            }
//            if (board.getTile(tileNumber - 8).isEmpty())
//                possibleMoves.add(tileNumber - 8);
//            if (board.getTile(tileNumber - 7).isOccupiedByOpponent(game.Color.WHITE)) {
//                possibleMoves.add(tileNumber - 7);
//            }
//            if (board.getTile(tileNumber - 9).isOccupiedByOpponent(game.Color.WHITE)) {
//                possibleMoves.add(tileNumber - 9);
//            }
//        } else if (this.color == game.Color.BLACK) {
//            if (row == 1) {
//                if (board.getTile(tileNumber + 16).isEmpty()
//                        && board.getTile(tileNumber + 8).isEmpty()) {
//                    possibleMoves.add(tileNumber + 16);
//                }
//            }
//            if (board.getTile(tileNumber + 8).isEmpty())
//                possibleMoves.add(tileNumber + 8);
//
//            if (board.getTile(tileNumber + 7).isOccupiedByOpponent(game.Color.BLACK)) {
//                possibleMoves.add(tileNumber + 7);
//            }
//            if (board.getTile(tileNumber + 9).isOccupiedByOpponent(game.Color.BLACK)) {
//                possibleMoves.add(tileNumber + 9);
//            }
//        }

        return possibleMoves;
    }

    @Override
    public List<Integer> findControlledTiles(Board board) {
        if (color == Color.WHITE){
            return new ArrayList<>(Arrays.asList(getTileNumber()-7, getTileNumber()-9));
        }
        else if(color == Color.BLACK)
        {
            return new ArrayList<>(Arrays.asList(getTileNumber()+7, getTileNumber()+9));
        }
        else{
            throw new IllegalArgumentException("Color is not black nor white");
        }
    }
}
