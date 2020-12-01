package com.example.chess.service;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.game.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MoveService {
    private MoveRepository moveRepository;

    public MoveService(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    public void addMove(Move toAdd){
        moveRepository.save(toAdd);
    }

    public void flushRepository(){
        moveRepository.flush();
    }

    public Move castToProperClass(MoveDto moveDto, Board board){
        return MoveFactory.getMove(moveDto, board);
    }

    public Move getLastAIMoveFromGame(int gameId){
        return moveRepository.findTopByGameIdOrderByIdDesc(gameId).orElseThrow();
    }
    public Optional<List<Move>> getAllMovesInGame(int gameId){
        return moveRepository.findAllByGameId(gameId);
    }

    public List<Move> saveAll( Iterable<Move> moves)    {
        return moveRepository.saveAll(moves);
    }




}
