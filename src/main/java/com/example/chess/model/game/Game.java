package com.example.chess.model.game;

import com.example.chess.model.pieces.Piece;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;

    @Transient
    Board board;

    Color humanPlayerColor;
    @Transient
    Color currentPlayer;

    boolean finished;
    Color wonByColor;

    Date dateFinished;

    @JsonManagedReference
    @OneToMany(mappedBy = "game",  cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Move> movesPlayed = new ArrayList<>();

    public Game() {
        board = new Board();
        humanPlayerColor = Color.WHITE;
        currentPlayer = Color.WHITE;
    }

    public Game(User user){
        this();
        this.user = user;
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


//    public Move showLastAIMove() {
//
//        return movesPlayed.get(movesPlayed.size() - 1);
//    }


    public Move makePlayerMove(Move moveToMake) {

        moveToMake = board.makeMove(moveToMake);
//        movesPlayed.add(moveToMake);
        checkIfFinished();
        currentPlayer = currentPlayer.getOpponentColor();
        return moveToMake;

    }
    public boolean checkIfFinished(){
        if(getBoardState().get(35) !=null){
            dateFinished = new Date();
            finished = true;
            wonByColor = currentPlayer;
        }
        return finished;
    }

    public Move makeAIMove() {

//        var moves = getPossibleCurrentPlayerMoves();
//        Random generator = new Random();
//        var keys = moves.keySet().toArray();
//        int randomKey = (int) keys[generator.nextInt(keys.length)];
//
//        List<Integer> pieceMoveList = moves.get(randomKey);
//        int randomValue = pieceMoveList.get(generator.nextInt(pieceMoveList.size()));

        Move toMake = AlgorithmAI.minmax(board, 4, false).getSecond();
        toMake = board.makeMove(toMake);

        checkIfFinished();
        currentPlayer = currentPlayer.getOpponentColor();
        return toMake;

    }
    public void castMoveTypes(){
        for(int i=0; i< movesPlayed.size(); i++){
            Move move = movesPlayed.get(i);
            if(move.getHash().equals("O-O")){
                CastlingMove castedMove = (CastlingMove) move;
                castedMove.calculateRookPosition(false);
                movesPlayed.set(i, castedMove);
            }
            if(move.getHash().equals("O-O-O")){
                CastlingMove castedMove = (CastlingMove) move;
                castedMove.calculateRookPosition(true);
                movesPlayed.set(i, castedMove);
            }
            if(move.getHash().contains("=")){
                PromotionMove castedMove = (PromotionMove) move;
                castedMove.setPromotedClass(move.getHash().charAt(move.getHash().length()-1));
                movesPlayed.set(i, castedMove);
            }
        }
    }
    public void calculateState() {
        this.castMoveTypes();
        List <Move> moves = movesPlayed;

        board.calculateState(moves);
        if(moves.size()==0){
            setCurrentPlayer(Color.WHITE);
        }
        else{
            setCurrentPlayer(getBoardState().get(moves.get(moves.size()-1).getDestinationTile())
                    .getColor().getOpponentColor());
        }

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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        finished = finished;
    }

    public Color getWonByColor() {
        return wonByColor;
    }

    public void setWonByColor(Color wonByColor) {
        this.wonByColor = wonByColor;
    }

    public Date getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(Date dateFinished) {
        this.dateFinished = dateFinished;
    }
}

