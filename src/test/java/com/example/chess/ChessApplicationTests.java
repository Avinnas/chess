package com.example.chess;

import com.example.chess.model.game.Board;
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

	@Test
	void boardTest(){
		Board board = new Board();
		System.out.println(board.getPiecesByType().get(0).values());
	}

}
