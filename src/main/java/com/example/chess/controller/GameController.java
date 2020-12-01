package com.example.chess.controller;


import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.game.Move;
import com.example.chess.model.pieces.Piece;
import com.example.chess.service.GameService;
import com.example.chess.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/game")
public class GameController {

    GameService gameService;
    UserService userService;

    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping
    public String showBoardState() {
        return "game";
    }


    @GetMapping("/current_state")
    @ResponseBody
    public HashMap<Integer, Piece> showCurrentState() {
        return gameService.getCurrentPlayerGameState();
    }

    @GetMapping("/player_pieces")
    @ResponseBody
    public List<Piece> showPlayerPieces() {
        return gameService.getPlayerPieces();
    }

    @GetMapping("/player_moves")
    @ResponseBody
    public HashMap<Integer, List<Integer>> showPlayerMoves() {
        return gameService.getPossibleCurrentPlayerMoves();
    }

    @PostMapping
    @ResponseBody
    public void newMove(@RequestBody MoveDto moveToMake)  {
        gameService.newMove(moveToMake);
    }

    @GetMapping("/last_move")
    @ResponseBody
    public Move showLastMove(){
        return gameService.getLastAIMove();
    }

    @GetMapping("/finished")
    @ResponseBody
    public boolean checkIfFinished(){
        return gameService.checkIfFinished();
    }



}
