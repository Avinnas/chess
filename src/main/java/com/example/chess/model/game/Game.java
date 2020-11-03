package com.example.chess.model.game;

import com.example.chess.model.pieces.Piece;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class Game {

    Board board;


    Color currentPlayer;
    List<Move> movesPlayed;

    public Game() {
        board = new Board();


    }


    public HashMap<Integer, Piece> getBoardState(){

        return board.getTilePieceAssignment();
    }

    public HashMap<?,?> getPossibleMoves(){

        return board.findAllPossibleMoves();
    }

}
