package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("Pawn")
public class Pawn extends Piece {
    public Pawn(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    @Override
    public List<Integer> findPossibleMoves(Board board) {
        List<Integer> possibleMoves = new ArrayList<>();
        int row = tileNumber / 8 +1;
        int column = tileNumber %8;
        if(this.color == Color.WHITE){
            int destinationTile = tileNumber -8;
            if(board.tileIsEmpty(destinationTile)){
                possibleMoves.add(destinationTile);
                destinationTile -= 8;
                if(row == 7 && (board.tileIsEmpty(destinationTile))){
                    possibleMoves.add(destinationTile);
                }
            }
            if(board.tileIsOccupiedByOpponent(tileNumber - 7, color) && column!=7){
                possibleMoves.add(tileNumber - 7);
            }
            if(board.tileIsOccupiedByOpponent(tileNumber - 9, color) && column!=0){
                possibleMoves.add(tileNumber - 9);
            }

        }

        else{

            int destinationTile = tileNumber + 8;
            if(board.tileIsEmpty(destinationTile)){
                possibleMoves.add(destinationTile);
                destinationTile += 8;
                if(row == 2 && (board.tileIsEmpty(destinationTile))){
                    possibleMoves.add(destinationTile);
                }
            }
            if(board.tileIsOccupiedByOpponent(tileNumber + 7, color) && column!=0){
                possibleMoves.add(tileNumber + 7);
            }
            if(board.tileIsOccupiedByOpponent(tileNumber + 9, color) && column!=7){
                possibleMoves.add(tileNumber + 9);
            }



        }

        return possibleMoves;
    }

    @Override
    public List<Integer> findControlledTiles(Board board) {

        throw new IllegalStateException("Controlled tiles for pawn not implemeted");
//        if (color == Color.WHITE){
//            return new ArrayList<>(Arrays.asList(getTileNumber()-7, getTileNumber()-9));
//        }
//        else if(color == Color.BLACK)
//        {
//            return new ArrayList<>(Arrays.asList(getTileNumber()+7, getTileNumber()+9));
//        }
//        else{
//            throw new IllegalArgumentException("Color is not black nor white");
//        }
    }
}
