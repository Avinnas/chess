package com.example.chess.model.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MoveRepository extends JpaRepository<Move, Integer>{
    public Optional<Move> findTopByGameIdOrderByIdDesc(int gameId);
    public Optional<List<Move>> findAllByGameId(int gameId);
}
