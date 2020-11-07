package com.example.chess;

import com.example.chess.model.dto.MoveDto;
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

		game.makePlayerMove(new MoveDto(55,47));
		game.makeAIMove();

		System.out.println(game.getBoardState());
	}

	@Test
	void boardTest(){
//		Board board = new Board();
//		System.out.println(board.getPiecesByType().get(0).values());
//
//		board.makeMove(55, 47);
//
//		System.out.println(board.getPiecesByType().get(0).get("Pawn"));
//		System.out.println(board.getTilePieceAssignment());
	}

}
