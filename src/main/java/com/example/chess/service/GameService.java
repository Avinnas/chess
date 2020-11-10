package com.example.chess.service;

import com.example.chess.model.game.Game;
import com.example.chess.model.game.GameRepository;
import com.example.chess.model.game.Move;
import com.example.chess.model.pieces.Piece;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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
        this.game = getOrCreateCurrentGame();
    }
    public Game getOrCreateCurrentGame(){
        Optional<Game> optionalGame = gameRepository.findTopByUserIdOrderByIdDesc(0);
        return optionalGame.orElseGet(()-> gameRepository.save(new Game()));
    }

    public HashMap<Integer, Piece> getCurrentPlayerGameState(){
        return getOrCreateCurrentGame().getBoardState();
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

        moveToMake.setGame(game);
        moveToMake = game.makePlayerMove(moveToMake);
        moveService.addMove(moveToMake);

        moveService.flushRepository();


        Move AIMove = game.makeAIMove();
        AIMove.setGame(game);
        moveService.addMove(AIMove);

    }

    public void newAIMove(){

    }
}
