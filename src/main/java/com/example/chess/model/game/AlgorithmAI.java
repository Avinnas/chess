package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.pieces.Piece;
import org.springframework.data.util.Pair;

import java.util.Map;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class AlgorithmAI {

    private final static Map<String, Integer> pieceValues = Map.of(
            "Rook", 500,
            "Queen", 900,
            "King", 3000,
            "Pawn", 100,
            "Bishop", 300,
            "Knight", 300
    );

    public static int evaluateBoard(Board board) {
        int sum = 0;
        for (Piece piece : board.getTilePieceAssignment().values()) {
            if (piece.getColor() == Color.WHITE) {
                sum += pieceValues.get(piece.getName());
            } else {
                sum -= pieceValues.get(piece.getName());
            }
        }
        return sum;
    }

    public static Pair<Integer, MoveDto> minmax(Board board, int depth, boolean isWhitePlayerTurn) {
        if (isWhitePlayerTurn) {
            return maximize(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            return minimize(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    public static Pair<Integer, MoveDto> maximize(Board board, int depth, int alpha, int beta) {
        if (depth == 0) {
            return Pair.of(evaluateBoard(board), new MoveDto());
        }

        int maxEval = Integer.MIN_VALUE;
        MoveDto bestMove = new MoveDto();
        var moves = board.findCurrentPlayerMoves(Color.WHITE);
        for (int startTile : moves.keySet()) {
            for (int destinationTile : moves.get(startTile)) {

                Board boardCopy = new Board(board);
                MoveDto m = new MoveDto(startTile, destinationTile);
                boardCopy.makeMove(m);

                int eval = minimize(boardCopy, depth - 1, alpha, beta).getFirst();
                if (maxEval < eval) {
                    maxEval = eval;
                    bestMove = m;
                }
                alpha = max(alpha, eval);
                if(beta<=alpha){
                    return Pair.of(maxEval, bestMove);
                }
            }
        }
        return Pair.of(maxEval, bestMove);
    }

    public static Pair<Integer, MoveDto> minimize(Board board, int depth, int alpha, int beta) {
        if (depth == 0) {
            return Pair.of(evaluateBoard(board), new MoveDto());
        }

        int minEval = Integer.MAX_VALUE;
        var moves = board.findCurrentPlayerMoves(Color.BLACK);
        MoveDto bestMove = new MoveDto();
        for (int startTile : moves.keySet()) {
            for (int destinationTile : moves.get(startTile)) {

                Board boardCopy = new Board(board);
                MoveDto m = new MoveDto(startTile, destinationTile);
                boardCopy.makeMove(m);

                int eval = maximize(boardCopy, depth - 1, alpha, beta).getFirst();
                if (minEval > eval) {
                    minEval = eval;
                    bestMove = m;
                }
                beta = min(beta, eval);
                if(beta<=alpha){
                    return Pair.of(minEval, bestMove);
                }


            }

        }
        return Pair.of(minEval, bestMove);
    }
}
