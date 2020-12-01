package com.example.chess;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.game.*;
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

//		game.makePlayerMove(new Move(55,47));
//		game.makeAIMove();

		System.out.println(game.getBoardState());
	}



	@Test
	void boardTest() {
		Board board = new Board();
		board.removePiece(62);
		board.removePiece(61);
		System.out.println(board.getPlayerPieces(Color.WHITE));
		var move = MoveFactory.getMove(new MoveDto(60,62), board);
		System.out.println(move.getClass());
		Class<? extends Move> c = move.getClass();
		System.out.println(c.cast(move).getClass().getName());;
		CastlingMove castlingMove = (CastlingMove) move;
		board.makeMove(castlingMove);
		System.out.println(board.getTilePieceAssignment());


	}
	@Test
	void minmaxTest(){
		Board board = new Board();

		System.out.println(AlgorithmAI.minmax(board,7, true));
	}


}
