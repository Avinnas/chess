package com.example.chess.model.game;

import com.example.chess.model.pieces.Piece;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Game {

    Board board;


    Color humanPlayerColor;

    Color currentPlayer;
    List<Move> movesPlayed;

    public Game() {
        board = new Board();
        humanPlayerColor = Color.WHITE;
        currentPlayer = Color.WHITE;

    }


    public HashMap<Integer, Piece> getBoardState(){

        return board.getTilePieceAssignment();
    }

    public HashMap<?,?> getPossibleCurrentPlayerMoves(){

        return board.findCurrentPlayerMoves(currentPlayer);
    }

    public Map<String, List<Piece>> getPlayerPieces(){
        return board.getPlayerPieces(humanPlayerColor);
    }

}
