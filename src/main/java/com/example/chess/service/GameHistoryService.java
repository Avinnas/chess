package com.example.chess.service;

import com.example.chess.model.game.ArchiveBoard;
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
    ArchiveBoard board;

    public GameHistoryService(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public Game getFinishedGame(int gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        board = new ArchiveBoard(game.getMovesPlayed());
        return game;
    }

    public List<Game> getGameHistory(){
        return gameRepository.findAllByUserIdAndFinishedIsTrue(userService.getCurrentPlayerId()).orElse(new ArrayList<>());
    }

    public Map<?,?> getBoardState(int moveId){
        board.calculateState(moveId);
        return board.getTilePieceAssignment();
    }

    public List<Integer> getFinishedGamesIds() {
        return getGameHistory().stream()
                .map(Game::getId)
                .collect(Collectors.toList());
    }
}
