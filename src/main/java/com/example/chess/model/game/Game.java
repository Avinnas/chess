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
    int gameResult;

    Date dateFinished;

    @JsonManagedReference
    @OneToMany(mappedBy = "game",  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
    public Game(User user, String color){
        this(user);
        if(color.equals("BLACK"))
            this.humanPlayerColor = Color.BLACK;
    }


    public HashMap<Integer, Piece> getBoardState() {

        return board.getTilePieceAssignment();
    }

    public HashMap<Integer, HashSet<Integer>> getPossibleCurrentPlayerMoves() {
        if(currentPlayer != humanPlayerColor){
            System.out.println(" RETURNED MOVES FOR AI ");
        }

        return board.findCurrentPlayerMoves(currentPlayer);
    }

    public  List<Piece> getPlayerPieces() {
        return board.getPlayerPieces(humanPlayerColor);
    }



    public Move makeMove(Move moveToMake) {

        board.makeMove(moveToMake);
        checkIfFinished();
        currentPlayer = currentPlayer.getOpponentColor();
        return moveToMake;

    }

    public boolean checkIfFinished(){
        int res= board.checkResult(currentPlayer);
        if(res!=0){
            dateFinished = new Date();
            finished = true;
            gameResult=res;
            return true;
        }
        if(positionRepetition()){
            dateFinished = new Date();
            finished = true;
            gameResult=10;
            return true;
        }
        return false;
    }

    public boolean positionRepetition(){
        if(movesPlayed.size() <8 )
            return false;
        int listSize = movesPlayed.size();
        if(movesPlayed.get(listSize-1).equals(movesPlayed.get(listSize-5))
                && movesPlayed.get(listSize-2).equals(movesPlayed.get(listSize-6))
                && movesPlayed.get(listSize-3).equals(movesPlayed.get(listSize-7))
                && movesPlayed.get(listSize-4).equals(movesPlayed.get(listSize-8))){
            return true;
        }
        return false;
    }


    public void castMoveTypes(){
        Board boardCopy = new Board(board);
        for(int i=0; i< movesPlayed.size(); i++){
            Move move = movesPlayed.get(i);

            Move castedMove = MoveFactory.getSimpleMove(move,boardCopy);

            boardCopy.makeMove(castedMove);
            movesPlayed.set(i, castedMove);
            }
    }
    public void calculateState(int moveNumber) {
        this.castMoveTypes();
        List <Move> moves = movesPlayed;

        board.calculateState(moves.subList(0, moveNumber));
        if(moves.size()==0){
            setCurrentPlayer(Color.WHITE);
        }
        else{
            setCurrentPlayer(getBoardState().get(moves.get(moveNumber-1).getDestinationTile())
                    .getColor().getOpponentColor());
        }

    }

    public void calculateState() {
        calculateState(movesPlayed.size());

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

    public int getGameResult() {
        return gameResult;
    }

    public void setGameResult(int gameResult) {
        this.gameResult = gameResult;
    }

    public Date getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(Date dateFinished) {
        this.dateFinished = dateFinished;
    }
}

