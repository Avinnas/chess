package com.example.chess.model.game;

import com.example.chess.model.pieces.Piece;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "games")
@Component
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;

    @Transient
    Board board;

    Color humanPlayerColor;
    Color currentPlayer;

    @JsonManagedReference
    @OneToMany(mappedBy = "game",  cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Move> movesPlayed = new ArrayList<>();

    public Game() {
        board = new Board();
        humanPlayerColor = Color.WHITE;
        currentPlayer = Color.WHITE;


    }


    public HashMap<Integer, Piece> getBoardState() {

        return board.getTilePieceAssignment();
    }

    public HashMap<Integer, List<Integer>> getPossibleCurrentPlayerMoves() {

        return board.findCurrentPlayerMoves(currentPlayer);
    }

    public Map<String, List<Piece>> getPlayerPieces() {
        return board.getPlayerPieces(humanPlayerColor);
    }


    public Move getLastAIMove() {

        return movesPlayed.get(movesPlayed.size() - 1);
    }


    public Move makePlayerMove(Move moveToMake) {

        currentPlayer = Color.BLACK;
        return board.makeMove(moveToMake);

    }

    public Move makeAIMove() {

        var moves = getPossibleCurrentPlayerMoves();
        Random generator = new Random();
        var keys = moves.keySet().toArray();
        int randomKey = (int) keys[generator.nextInt(keys.length)];

        List<Integer> pieceMoveList = moves.get(randomKey);
        int randomValue = pieceMoveList.get(generator.nextInt(pieceMoveList.size()));

        Move toMake = board.makeMove(randomKey, randomValue);
        currentPlayer = Color.WHITE;

        return toMake;

    }


    public User getUser() {
        return user;
    }

    public List<Move> getMovesPlayed() {
        return movesPlayed;
    }

    public void setMovesPlayed(List<Move> movesPlayed) {
        this.movesPlayed = movesPlayed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Color getHumanPlayerColor() {
        return humanPlayerColor;
    }

    public void setHumanPlayerColor(Color humanPlayerColor) {
        this.humanPlayerColor = humanPlayerColor;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Color currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}

