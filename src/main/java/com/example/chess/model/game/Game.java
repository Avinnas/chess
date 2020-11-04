package com.example.chess.model.game;

import com.example.chess.model.pieces.Piece;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Game {

    Board board;


    Color playerColor;
    Color currentPlayer;
    List<Move> movesPlayed;

    public Game() {
        board = new Board();
        playerColor = Color.WHITE;

    }


    public HashMap<Integer, Piece> getBoardState(){

        return board.getTilePieceAssignment();
    }

    public HashMap<?,?> getPossibleMoves(){

        return board.findAllPossibleMoves();
    }

    public Map<String, List<Piece>> getPlayerPieces(){
        return board.getPlayerPieces(playerColor);
    }

}
