package com.example.chess.controller;

import com.example.chess.service.GameHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/history")
public class HistoryController {

    GameHistoryService gameHistoryService;

    public HistoryController(GameHistoryService gameHistoryService) {
        this.gameHistoryService = gameHistoryService;
    }

    @GetMapping
    public String showPlayerGamesHistory(Model model){
        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("finishedGames", gameHistoryService.getFinishedGamesIds());
        return "history";
    }
    @GetMapping("/{id}")
    public String showFinishedGame(Model model, @PathVariable int id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("game", gameHistoryService.getFinishedGame(id));
        return "archive_game";
    }

//    @GetMapping
//    @ResponseBody
//    public List<Game> showPlayerGamesHistory(){
//        return gameHistoryService.getGameHistory();
//    }
//    @GetMapping("/{id}")
//    @ResponseBody
//    public Game showFinishedGame(@PathVariable int id){
//        return gameHistoryService.getFinishedGame(id);
//    }

    @GetMapping("/move/{moveId}")
    @ResponseBody
    public Map<?,?> showBoardState(Model model, @PathVariable int moveId){
        return gameHistoryService.getBoardState(moveId);
    }
}
