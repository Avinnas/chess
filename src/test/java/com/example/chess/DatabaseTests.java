package com.example.chess;

import com.example.chess.model.game.Game;
import com.example.chess.model.game.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@DataJpaTest
public class DatabaseTests {

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired
    GameRepository gameRepository;

    @Test
    void databaseTest(){
        gameRepository.save(new Game());
        var a = gameRepository.findById(0);

        System.out.println(a);
    }
}
