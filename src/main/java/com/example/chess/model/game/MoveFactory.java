package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;

import static java.lang.Math.abs;

public class MoveFactory {
    public static Move getMove(MoveDto moveDto, Board board){
        var move = getSimpleMove(moveDto, board);
        move.calculateHash(board);
        return move;
    }

    public static Move getSimpleMove(MoveDto moveDto, Board board) {
        if(board.getPiece(moveDto.getStartTile()).getName().equals("King")
                && abs(moveDto.getDestinationTile()- moveDto.getStartTile())==2){
            return new CastlingMove(moveDto);
        }
        else if(board.getPiece(moveDto.getStartTile()).getName().equals("Pawn")
                && ((moveDto.getDestinationTile() /8)==0 || (moveDto.getDestinationTile() /7)==0)){
            return new PromotionMove(moveDto, 'Q');
        }
        return new Move(moveDto);
    }
}
