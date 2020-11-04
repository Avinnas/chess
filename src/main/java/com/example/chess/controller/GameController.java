package com.example.chess.controller;


import com.example.chess.model.game.Color;
import com.example.chess.model.game.Game;
import com.example.chess.model.pieces.Bishop;
import com.example.chess.model.pieces.Piece;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


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
        return "index";
    }

    @GetMapping("/player_pieces")
    public Map<?,?> showPlayerPieces(int tile){
        return game.getPlayerPieces();
    }


}
