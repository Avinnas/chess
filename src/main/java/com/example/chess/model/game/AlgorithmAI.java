package com.example.chess.model.game;

import com.example.chess.model.pieces.Piece;
import org.springframework.data.util.Pair;

import java.util.*;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class AlgorithmAI {

    private final static Map<String, Integer> pieceValues = Map.of(
            "Rook", 500,
            "Queen", 900,
            "King", 6000,
            "Pawn", 100,
            "Bishop", 330,
            "Knight", 320
    );

    private final static short[] pawnValueTable = new short[]
            {0, 0, 0, 0, 0, 0, 0, 0,
                    50, 50, 50, 50, 50, 50, 50, 50,
                    10, 10, 20, 30, 30, 20, 10, 10,
                    5, 5, 10, 25, 25, 10, 5, 5,
                    0, 0, 0, 20, 20, 0, 0, 0,
                    5, -5, -10, 0, 0, -10, -5, 5,
                    5, 10, 10, -20, -20, 10, 10, 5,
                    0, 0, 0, 0, 0, 0, 0, 0};

    private final static short[] knightValueTable = new short[]
            {-50, -40, -30, -30, -30, -30, -40, -50,
                    -40, -20, 0, 0, 0, 0, -20, -40,
                    -30, 0, 10, 15, 15, 10, 0, -30,
                    -30, 5, 15, 20, 20, 15, 5, -30,
                    -30, 0, 15, 20, 20, 15, 0, -30,
                    -30, 5, 10, 15, 15, 10, 5, -30,
                    -40, -20, 0, 5, 5, 0, -20, -40,
                    -50, -40, -30, -30, -30, -30, -40, -50};

    private final static short[] queenValueTable = new short[]
            {-20, -10, -10, -5, -5, -10, -10, -20,
                    -10, 0, 0, 0, 0, 0, 0, -10,
                    -10, 0, 5, 5, 5, 5, 0, -10,
                    -5, 0, 5, 5, 5, 5, 0, -5,
                    0, 0, 5, 5, 5, 5, 0, -5,
                    -10, 5, 5, 5, 5, 5, 0, -10,
                    -10, 0, 5, 0, 0, 0, 0, -10,
                    -20, -10, -10, -5, -5, -10, -10, -20};

    private final static short[] rookValueTable = new short[]
            {0, 0, 0, 0, 0, 0, 0, 0,
                    5, 10, 10, 10, 10, 10, 10, 5,
                    -5, 0, 0, 0, 0, 0, 0, -5,
                    -5, 0, 0, 0, 0, 0, 0, -5,
                    -5, 0, 0, 0, 0, 0, 0, -5,
                    -5, 0, 0, 0, 0, 0, 0, -5,
                    -5, 0, 0, 0, 0, 0, 0, -5,
                    0, 0, 0, 5, 5, 0, 0, 0};

    private final static short[] bishopValueTable = new short[]
            {-20, -10, -10, -10, -10, -10, -10, -20,
                    -10, 0, 0, 0, 0, 0, 0, -10,
                    -10, 0, 5, 10, 10, 5, 0, -10,
                    -10, 5, 5, 10, 10, 5, 5, -10,
                    -10, 0, 10, 10, 10, 10, 0, -10,
                    -10, 10, 10, 10, 10, 10, 10, -10,
                    -10, 5, 0, 0, 0, 0, 5, -10,
                    -20, -10, -10, -10, -10, -10, -10, -20};

    private final static short[] kingValueTable = new short[]
            {-30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -20, -30, -30, -40, -40, -30, -30, -20,
                    -10, -20, -20, -20, -20, -20, -20, -10,
                    20, 20, 0, 0, 0, 0, 20, 20,
                    20, 30, 10, 0, 0, 10, 30, 20};

    private final static Map<String, short[]> positionalValues = Map.of(
            "Pawn", pawnValueTable,
            "Knight", knightValueTable,
            "King", kingValueTable,
            "Queen", queenValueTable,
            "Bishop", bishopValueTable,
            "Rook", rookValueTable
    );

    public static int getPositionalValue(Piece piece) {
        return (positionalValues
                .get(piece.getName())[
                        piece.getColor() == Color.WHITE ? piece.getTileNumber() : getMirrorTile(piece.getTileNumber())
                ]);
    }

    public static int getMirrorTile(int tile) {
        int row = tile / 8;
        return tile % 8 + 8 * (7 - row);
    }

    public static List<Move> generateListOfMoves(HashMap<Integer, HashSet<Integer>> moveMap, Board board){
        List<Move> captures = new ArrayList<>();
        List<Move> nonCaptures = new ArrayList<>();
        for(var entry: moveMap.entrySet()){
            for(int destinationTile: entry.getValue()){
                Move m = MoveFactory.getSimpleMove(entry.getKey(), destinationTile, board);
                if(m.isCapture(board)){
                    captures.add(m);
                }
                else {
                    nonCaptures.add(m);
                }
            }
        }
        captures.addAll(nonCaptures);
        return captures;
    }

    public static int evaluateBoard(Board board) {
        int result = board.getGameResult();
        if(result == 10){
            return 0;
        } else if (result == 1){
            return 100000;
        } else if (result == -1){
            return -100000;
        }

        int sum = 0;
        for (Piece piece : board.getTilePieceAssignment().values()) {
            if (piece.getColor() == Color.WHITE) {
                sum += pieceValues.get(piece.getName()) + getPositionalValue(piece);
            } else {
                sum = sum - pieceValues.get(piece.getName()) - getPositionalValue(piece);
            }
        }
        return sum;
    }

    public static Pair<Integer, Move> minmax(Board board, int depth, boolean isWhitePlayerTurn) {
        if (isWhitePlayerTurn) {
            return maximize(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            return minimize(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    public static Pair<Integer, Move> maximize(Board board, int depth, int alpha, int beta) {
        if (depth == 0) {
            return Pair.of(evaluateBoard(board), new Move());
        }

        int maxEval = Integer.MIN_VALUE;
        Move bestMove = new Move();
        var movesMap = board.findCurrentPlayerMoves(Color.WHITE, true);

        if (board.getGameResult()!=0){
            return Pair.of(evaluateBoard(board), new Move());
        }
        var moves = generateListOfMoves(movesMap,board);

        for(Move m: moves) {
            Board boardCopy = new Board(board);
            boardCopy.makeMove(m);

            int eval = minimize(boardCopy, depth - 1, alpha, beta).getFirst();
            if (maxEval < eval) {
                maxEval = eval;
                bestMove = m;
            }
            alpha = max(alpha, eval);
            if (beta <= alpha) {
                return Pair.of(maxEval, bestMove);
            }
        }
        return Pair.of(maxEval, bestMove);
    }

    public static Pair<Integer, Move> minimize(Board board, int depth, int alpha, int beta) {
        if (depth == 0) {
            return Pair.of(evaluateBoard(board), new Move());
        }

        int minEval = Integer.MAX_VALUE;
        var movesMap = board.findCurrentPlayerMoves(Color.BLACK, true);
        Move bestMove = new Move();

        if (board.getGameResult()!=0){
            return Pair.of(evaluateBoard(board), new Move());
        }

        var moves = generateListOfMoves(movesMap,board);

        for(Move m: moves) {
            Board boardCopy = new Board(board);
            boardCopy.makeMove(m);

            int eval = maximize(boardCopy, depth - 1, alpha, beta).getFirst();
            if (minEval > eval) {
                minEval = eval;
                bestMove = m;
            }
            beta = min(beta, eval);
            if (beta <= alpha) {
                return Pair.of(minEval, bestMove);
            }
        }
        return Pair.of(minEval, bestMove);
    }
}
