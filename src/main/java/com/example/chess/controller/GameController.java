package com.example.chess.controller;


import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.game.Game;
import com.example.chess.model.game.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/game")
public class GameController {

    Game game;
    GameRepository gameRepository;

    public GameController(Game game, GameRepository gameRepository) {
        this.game = game;
        this.gameRepository = gameRepository;
    }

    @GetMapping
    public String showBoardState() {
        return "game";
    }

    public String toJSON(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @GetMapping("/current_state")
    @ResponseBody
    public String showCurrentState() throws JsonProcessingException{
        return toJSON(game.getBoardState());
    }

    @GetMapping("/player_pieces")
    @ResponseBody
    public String showPlayerPieces() throws JsonProcessingException {
        return toJSON(game.getPlayerPieces());
    }

    @GetMapping("/player_moves")
    @ResponseBody
    public String showPlayerMoves() throws JsonProcessingException {
        return toJSON(game.getPossibleCurrentPlayerMoves());
    }

    @PostMapping
    @ResponseBody
    public void newMove(@RequestBody MoveDto moveToMake){
        game.newMove(moveToMake);

    }

    @GetMapping("/last_move")
    @ResponseBody
    public MoveDto showLastMove(){
        return game.getLastAIMove();
    }



}
