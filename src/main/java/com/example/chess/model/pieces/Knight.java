package com.example.chess.model.pieces;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonTypeName("Knight")
public class Knight extends Piece {

    public Knight(int tileNumber, Color color) {
        super(tileNumber, color);
    }

    @Override
    public List<Integer> findPossibleMoves(Board board) {
        int[] relativePossibleMoves = {-17, -15, -10, -6, 6, 10, 15, 17};
        return Arrays.stream(relativePossibleMoves)

                // TODO - ruchy podczas stania na krawedzi/w rogu
                .filter(x -> x + tileNumber >= 0)
                .filter(x -> x + tileNumber < 64)
                .boxed()
                .collect(Collectors.toList());
    }

}
