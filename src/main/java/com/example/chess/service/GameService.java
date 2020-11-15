package com.example.chess.service;

import com.example.chess.model.game.*;
import com.example.chess.model.pieces.Piece;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class GameService {

    Game game;
    GameRepository gameRepository;
    UserService userService;
    MoveService moveService;


    public GameService(GameRepository gameRepository, UserService userService, MoveService moveService) {

        this.gameRepository = gameRepository;
        this.userService = userService;
        this.moveService = moveService;
    }
    public Game getOrCreateCurrentGame(){
        User user = userService.getCurrentPlayer();
        Optional<Game> optionalGame = gameRepository.findTopByUserIdAndFinishedIsFalseOrderByIdDesc(0);
        if(optionalGame.isPresent()){
            Game game = optionalGame.get();
            game.calculateState();
            return game;
        }
        else{
            return gameRepository.save(new Game(user));
        }
    }

    public HashMap<Integer, Piece> getCurrentPlayerGameState(){
        this.game = getOrCreateCurrentGame();

        // TODO - obsługa przypadku, kiedy ostatni ruch był gracza i komputer musi go wykonać.

        return game.getBoardState();
    }

    public Map<String, List<Piece>> getPlayerPieces(){
        return game.getPlayerPieces();
    }

    public HashMap<Integer, List< Integer>> getPossibleCurrentPlayerMoves() {
        return game.getPossibleCurrentPlayerMoves();
    }

    public Move getLastAIMove(){
        if (game.getCurrentPlayer() != game.getHumanPlayerColor()) {
            throw new IllegalStateException();
        }
        return moveService.getLastAIMoveFromGame(game.getId());
    }

    public void newMove(Move moveToMake) {

        moveToMake = game.makePlayerMove(moveToMake);
        moveToMake.setGame(game);
        moveService.addMove(moveToMake);
        if(game.isFinished()){
            gameRepository.save(game);
            gameRepository.flush();
            return;
        }

        Move AIMove = game.makeAIMove();
        AIMove.setGame(game);
        moveService.addMove(AIMove);

        if(game.isFinished()){
            gameRepository.save(game);
        }

    }

    public boolean checkIfFinished(){
        return game.isFinished();
    }


}
