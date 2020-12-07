package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.HashSet;

@JsonTypeName("Pawn")
public class Pawn extends Piece {
    public Pawn(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    @Override
    public HashSet<Integer> findPossibleMoves(Board board) {
        HashSet<Integer> possibleMoves = new HashSet<>();
        int row = tileNumber / 8 + 1;
        int column = tileNumber % 8;
        if (this.color == Color.WHITE) {
            int destinationTile = tileNumber - 8;
            if (board.tileIsEmpty(destinationTile) && destinationTile>=0 && destinationTile <64) {
                possibleMoves.add(destinationTile);
                destinationTile -= 8;
                if (row == 7 && (board.tileIsEmpty(destinationTile))  && destinationTile>=0F) {
                    possibleMoves.add(destinationTile);
                }
            }
            if (board.tileIsOccupiedByOpponent(tileNumber - 7, color) && column != 7) {
                possibleMoves.add(tileNumber - 7);
            }
            if (board.tileIsOccupiedByOpponent(tileNumber - 9, color) && column != 0) {
                possibleMoves.add(tileNumber - 9);
            }

        } else {

            int destinationTile = tileNumber + 8;
            if (board.tileIsEmpty(destinationTile)  && destinationTile>=0 && destinationTile <64) {
                possibleMoves.add(destinationTile);
                destinationTile += 8;
                if (row == 2 && (board.tileIsEmpty(destinationTile)) && destinationTile <64) {
                    possibleMoves.add(destinationTile);
                }
            }
            if (board.tileIsOccupiedByOpponent(tileNumber + 7, color) && column != 0) {
                possibleMoves.add(tileNumber + 7);
            }
            if (board.tileIsOccupiedByOpponent(tileNumber + 9, color) && column != 7) {
                possibleMoves.add(tileNumber + 9);
            }


        }

        return possibleMoves;
    }

    @Override
    public HashSet<Integer> findPossibleMoves(Board board, boolean includeControlledTiles) {
        return includeControlledTiles ? findControlledTiles(board) : findPossibleMoves(board);
    }

    @Override
    public HashSet<Integer> findControlledTiles(Board board) {
        HashSet<Integer> controlledTiles = new HashSet<>();

        int column = tileNumber % 8;
        if (this.color == Color.WHITE) {

            if (column != 7) {
                controlledTiles.add(tileNumber - 7);
            }
            if (column != 0) {
                controlledTiles.add(tileNumber - 9);
            }
        } else {
            if (column != 0) {
                controlledTiles.add(tileNumber + 7);
            }
            if (column != 7) {
                controlledTiles.add(tileNumber + 9);
            }

        }
        return controlledTiles;
    }
}

