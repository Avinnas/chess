package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.*;

@JsonTypeName("King")
public class King extends Piece {
    public King(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    public HashSet<Integer> findCastlingMoves(Board board) {
        HashSet<Integer> tilesControlled = new HashSet<>();
//        HashSet<Integer> tilesControlled = board.findTilesControlled(color.getOpponentColor()).values()
//                .stream()
//                .reduce((x,y) -> {
//                    x.addAll(y);
//                    return x;
//                }).orElseThrow();

        HashSet<Integer> moves = new HashSet<>();
        if (board.longCastlingAllowed(color)) {
            if (board.tileIsEmpty(tileNumber - 1) && !tilesControlled.contains(tileNumber - 1) &&
                    board.tileIsEmpty(tileNumber - 2) && !tilesControlled.contains(tileNumber - 2) &&
                    board.tileIsEmpty(tileNumber - 3) && !tilesControlled.contains(tileNumber - 3)) {
                moves.add(tileNumber - 2);
            }
        }
        if (board.shortCastlingAllowed(color)) {
            if (board.tileIsEmpty(tileNumber + 1) && !tilesControlled.contains(tileNumber + 1) &&
                    board.tileIsEmpty(tileNumber + 2) && !tilesControlled.contains(tileNumber + 2)) {
                moves.add(tileNumber + 2);
            }
        }
        return moves;
    }

    @Override
    public HashSet<Integer> findPossibleMoves(Board board){
        return findPossibleMoves(board, false);
    }

    public HashSet<Integer> findPossibleMoves(Board board, boolean includeControlledTiles) {
        List<Integer> relativePossibleMoves = new LinkedList<>(Arrays.asList(-7, -8, -9, -1, 1, 7, 8, 9));
        HashSet<Integer> possibleMoves = new HashSet<>();
        if(!includeControlledTiles){
            possibleMoves = findCastlingMoves(board);
        }
        int column = this.tileNumber % 8;
        if (column == 0) {
            relativePossibleMoves.remove(Integer.valueOf(-9));
            relativePossibleMoves.remove(Integer.valueOf(-1));
            relativePossibleMoves.remove(Integer.valueOf(7));
        }
        if (column == 7) {
            relativePossibleMoves.remove(Integer.valueOf(9));
            relativePossibleMoves.remove(Integer.valueOf(1));
            relativePossibleMoves.remove(Integer.valueOf(-7));
        }

        for (int tile : relativePossibleMoves) {
            int realTileNumber = tile + this.tileNumber;
            if (realTileNumber < 64 && realTileNumber >= 0 &&
                    (board.tileIsEmpty(realTileNumber)
                            || board.tileIsOccupiedByOpponent(realTileNumber, this.color)
                    || includeControlledTiles)) {
                possibleMoves.add(realTileNumber);
            }
        }


        return possibleMoves;
    }
}
