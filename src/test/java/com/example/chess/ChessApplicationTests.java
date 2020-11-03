package com.example.chess;

import com.example.chess.controller.GameController;
import com.example.chess.model.game.Game;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChessApplicationTests {

	@Test
	void contextLoads() {

	}

	@Test
	void gameTest(){
		Game game = new Game();
		System.out.println(game.getBoardState());
	}

}
