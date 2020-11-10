package com.example.chess.model.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    public Optional<Game> findTopByUserIdOrderByIdDesc(int id);
}
