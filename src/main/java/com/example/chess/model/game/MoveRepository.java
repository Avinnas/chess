package com.example.chess.model.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MoveRepository extends JpaRepository<Move, Integer>{
}
