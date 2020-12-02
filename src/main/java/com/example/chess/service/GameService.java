package com.example.chess.service;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.game.*;
import com.example.chess.model.pieces.Piece;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    }

    public Game getOrCreateCurrentGame() {
        User user = userService.getCurrentPlayer();
        Optional<Game> optionalGame = gameRepository.findTopByUserIdAndFinishedIsFalseOrderByIdDesc(0);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();

            game.calculateState();
            return game;
        } else {
            return gameRepository.save(new Game(user));
        }
    }

    public HashMap<Integer, Piece> getCurrentPlayerGameState() {
        if(game == null){
            this.game = getOrCreateCurrentGame();
        }

        // TODO - obsługa przypadku, kiedy ostatni ruch był gracza i komputer musi go wykonać.

        return game.getBoardState();
    }

    public List<Piece> getPlayerPieces() {
        return game.getPlayerPieces();
    }

    public HashMap<Integer, HashSet<Integer>> getPossibleCurrentPlayerMoves() {
        return game.getPossibleCurrentPlayerMoves();
    }

    public Move getLastAIMove() {
        if (game.getCurrentPlayer() != game.getHumanPlayerColor()) {
            throw new IllegalStateException();
        }
        return moveService.getLastAIMoveFromGame(game.getId());
    }

    public void newMove(MoveDto moveDtoToMake) {

        Move moveToMake = moveService.castToProperClass(moveDtoToMake, game.getBoard());
        moveToMake.setGame(game);
        moveService.addMove(moveToMake);
        game.makeMove(moveToMake);

        if(game.isFinished()){
            gameRepository.save(game);
        }

    }
    public Move makeAIMove(){
        MoveDto moveDto = AlgorithmAI.minmax(game.getBoard(), 5, false).getSecond();
        Move moveToMake = MoveFactory.getMove(moveDto, game.getBoard());
        moveToMake.setGame(game);
        moveService.addMove(moveToMake);
        game.makeMove(moveToMake);

        gameRepository.save(game);

        return moveToMake;
    }

    public boolean checkAndRemoveIfFinished() {
        boolean finished = game.isFinished();
        if(finished)
            game=null;
        return finished;
    }


}
