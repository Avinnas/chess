package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;

import static java.lang.Math.abs;

public class MoveFactory {
    public static Move getMove(MoveDto move, Board board){
        var m = getSimpleMove(move.getStartTile(), move.getDestinationTile(), board);
        m.calculateHash(board);
        return m;
    }
    public static Move getMove(Move move, Board board){
        Move m = getSimpleMove(move,board);
        m.calculateHash(board);
        return m;
    }


    public static Move getSimpleMove(Move move, Board board) {
        return getSimpleMove(move.getStartTile(), move.getDestinationTile(), board);
    }

    public static Move getSimpleMove(int startTile, int destinationTile, Board board){
        if(board.getPiece(startTile).getName().equals("King")
                && abs(destinationTile- startTile)==2){
            return new CastlingMove(startTile, destinationTile);
        }
        else if(board.getPiece(startTile).getName().equals("Pawn")
                && ((destinationTile /8)==0 || (destinationTile /8)==7)){
            return new PromotionMove(startTile, destinationTile, 'Q');
        }
        return new Move(startTile, destinationTile);
    }
}
