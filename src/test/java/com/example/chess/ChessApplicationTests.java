package com.example.chess;

import com.example.chess.model.game.AlgorithmAI;
import com.example.chess.model.game.Board;
import com.example.chess.model.game.Game;
import com.example.chess.model.game.Move;
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

		game.makePlayerMove(new Move(55,47));
		game.makeAIMove();

		System.out.println(game.getBoardState());
	}



	@Test
	void boardTest() {
		Board board = new Board();


		board.makeMove(63,0);


	}
	@Test
	void minmaxTest(){
		Board board = new Board();

		System.out.println(AlgorithmAI.minmax(board,5, true));
	}


}
