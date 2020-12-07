package com.example.chess.service;

import com.example.chess.model.game.Board;
import com.example.chess.model.game.Game;
import com.example.chess.model.game.GameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameHistoryService {

    GameRepository gameRepository;
    UserService userService;
    Game game;

    public GameHistoryService(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public Game getFinishedGame(int gameId) {
        game = gameRepository.findById(gameId).orElseThrow();
        return game;
    }

    public List<Game> getGameHistory(){
        return gameRepository.findAllByUserId(userService.getCurrentPlayerId()).orElse(new ArrayList<>());
    }

    public Map<?,?> getBoardState(int moveId){
        game.setBoard(new Board());
        game.calculateState(moveId);
        return game.getBoardState();
    }

    public List<Integer> getFinishedGamesIds() {
        return getGameHistory().stream()
                .map(Game::getId)
                .collect(Collectors.toList());
    }
}
