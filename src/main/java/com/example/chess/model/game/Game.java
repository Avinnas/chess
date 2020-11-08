package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.pieces.Piece;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.*;

@Component
@Entity
public class Game {

    @Id
    int id;

    @ManyToOne
    Player player;

    @Transient
    Board board;

    Color humanPlayerColor;
    Color currentPlayer;

    @Transient
    List<Move> movesPlayed;

    public Game() {
        board = new Board();
        humanPlayerColor = Color.WHITE;
        currentPlayer = Color.WHITE;
        movesPlayed = new ArrayList<>();

    }


    public HashMap<Integer, Piece> getBoardState(){

        return board.getTilePieceAssignment();
    }

    public HashMap<Integer, List<Integer>> getPossibleCurrentPlayerMoves(){

        return board.findCurrentPlayerMoves(currentPlayer);
    }

    public Map<String, List<Piece>> getPlayerPieces(){
        return board.getPlayerPieces(humanPlayerColor);
    }


    public MoveDto getLastAIMove(){
        if (currentPlayer == Color.BLACK){
            throw new IllegalStateException();
        }
        Move lastMove = movesPlayed.get(movesPlayed.size()-1);
        return new MoveDto(lastMove);
    }


    public void newMove(MoveDto moveToMake){
        makePlayerMove(moveToMake);
        makeAIMove();
    }

    public void makePlayerMove(MoveDto moveToMake){
        movesPlayed.add(board.makeMove(moveToMake));
        currentPlayer = Color.BLACK;


    }

    public void makeAIMove(){

        var moves = getPossibleCurrentPlayerMoves();
        Random generator = new Random();
        var keys =  moves.keySet().toArray();
        int randomKey = (int) keys[generator.nextInt(keys.length)];

        List<Integer> pieceMoveList = moves.get(randomKey);
        int randomValue = pieceMoveList.get(generator.nextInt(pieceMoveList.size()));

        movesPlayed.add(board.makeMove(randomKey, randomValue));
        currentPlayer = Color.WHITE;

    }


}
