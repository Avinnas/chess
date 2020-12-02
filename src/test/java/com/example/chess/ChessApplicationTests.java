package com.example.chess;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.game.*;
import com.example.chess.model.pieces.King;
import com.example.chess.model.pieces.Pawn;
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
	void controlledTilesTest() {
		Board board = new Board();
		System.out.println(board.findTilesControlled(Color.BLACK));


	}

	@Test
	void minmaxTest(){
		Board board = new Board();

		System.out.println(AlgorithmAI.minmax(board,6, true));
	}

	@Test
	void positionalValueTest(){

		System.out.println(AlgorithmAI.getMirrorTile(50));
		System.out.println(AlgorithmAI.getMirrorTile(2));
		System.out.println(AlgorithmAI.getMirrorTile(63));
		System.out.println(AlgorithmAI.getMirrorTile(0));
		System.out.println(AlgorithmAI.getMirrorTile(25));
		System.out.println(AlgorithmAI.getMirrorTile(35));


		System.out.println(AlgorithmAI.getPositionalValue(new Pawn(35,Color.WHITE)));

		System.out.println(AlgorithmAI.getPositionalValue(new King(63,Color.BLACK)));
		System.out.println(AlgorithmAI.getPositionalValue(new King(63,Color.WHITE)));
	}


}
