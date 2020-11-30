package com.example.chess.model.game;

import com.example.chess.model.pieces.Piece;
import org.springframework.data.util.Pair;

import java.util.Map;

public class AlgorithmAI {

    private final static Map<String, Integer> pieceValues = Map.of(
            "Rook", 5,
            "Queen", 9,
            "King", 30,
            "Pawn", 1,
            "Bishop", 3,
            "Knight", 3
    );

    public static int evaluateBoard(Board board){
        int sum = 0;
        for(Piece piece: board.getTilePieceAssignment().values()){
            if(piece.getColor()==Color.WHITE){
                sum+=pieceValues.get(piece.getName());
            }
            else{
                sum-=pieceValues.get(piece.getName());
            }
        }
        return sum;
    }

    public static Pair<Integer, Move> minmax(Board board, int depth, boolean isWhitePlayerTurn){
        if(isWhitePlayerTurn){
            return maximize(board, depth);
        }
        else{
            return minimize(board, depth);
        }
    }

    public static Pair<Integer, Move> maximize(Board board, int depth){
        if(depth==0){
            return Pair.of(evaluateBoard(board), new Move());
        }

        int maxEval = Integer.MIN_VALUE;
        Move bestMove = new Move();
        var moves = board.findCurrentPlayerMoves(Color.WHITE);
        for(int startTile: moves.keySet()){
            for(int destinationTile : moves.get(startTile)){

                Board boardCopy = new Board(board);
                Move m = new Move(startTile, destinationTile);
                boardCopy.makeMove(m);

                int eval = minimize(boardCopy, depth-1).getFirst();
                if(maxEval < eval){
                    maxEval = eval;
                    bestMove = m;
                }
            }
        }
        return Pair.of(maxEval,bestMove);
    }

    public static Pair<Integer, Move> minimize(Board board, int depth){
        if(depth==0){
            return Pair.of(evaluateBoard(board), new Move());
        }

        int minEval = Integer.MAX_VALUE;
        var moves = board.findCurrentPlayerMoves(Color.BLACK);
        Move bestMove = new Move();
        for(int startTile: moves.keySet()){
            for(int destinationTile : moves.get(startTile)){

                Board boardCopy = new Board(board);
                Move m = new Move(startTile, destinationTile);
                boardCopy.makeMove(startTile, destinationTile);

                int eval = maximize(boardCopy, depth-1).getFirst();
                if(minEval > eval){
                    minEval = eval;
                    bestMove = m;
                }

            }
        }
        return Pair.of(minEval,bestMove);
    }
}
