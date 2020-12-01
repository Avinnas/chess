package com.example.chess.model.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    public Optional<Game> findTopByUserIdAndFinishedIsFalseOrderByIdDesc(int id );
    public Optional<List<Game>> findAllByUserIdAndFinishedIsTrue(int userId);
    public Optional<List<Game>> findAllByUserId(int userId);
}
