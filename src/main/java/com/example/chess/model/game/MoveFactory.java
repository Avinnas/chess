package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;

import static java.lang.Math.abs;

public class MoveFactory {
    public static Move getMove(MoveDto moveDto, Board board){
        var move = getSimpleMove(moveDto, board);
        move.calculateHash(board);
        return move;
    }
    public static Move getMove(Move move, Board board){
        move.calculateHash(board);
        return move;
    }


    public static Move getSimpleMove(MoveDto moveDto, Board board) {
        return getSimpleMove(moveDto.getStartTile(), moveDto.getDestinationTile(), board);
    }

    public static Move getSimpleMove(int startTile, int destinationTile, Board board){
        if(board.getPiece(startTile).getName().equals("King")
                && abs(destinationTile- startTile)==2){
            return new CastlingMove(startTile, destinationTile);
        }
        else if(board.getPiece(startTile).getName().equals("Pawn")
                && ((destinationTile /8)==0 || (destinationTile /7)==0)){
            return new PromotionMove(startTile, destinationTile, 'Q');
        }
        return new Move(startTile, destinationTile);
    }
}
