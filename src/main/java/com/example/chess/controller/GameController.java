package com.example.chess.controller;


import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.game.Color;
import com.example.chess.model.game.Game;
import com.example.chess.model.pieces.Bishop;
import com.example.chess.model.pieces.Piece;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/game")
public class GameController {

    Game game;
    public GameController(Game game) {
        this.game = game;
    }

    @GetMapping
    public String showBoardState(Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(game.getBoardState());

        Piece piece = new Bishop(12, Color.WHITE);
        String p = mapper.writeValueAsString(piece);
        model.addAttribute("boardTiles", s);
        model.addAttribute("string",p);
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
