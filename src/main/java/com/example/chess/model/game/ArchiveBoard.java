package com.example.chess.model.game;

import com.example.chess.model.pieces.Piece;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchiveBoard extends Board {

    Map<Integer, Piece> capturedPieces;
    int currentMove;
    Move[] moves;

    public ArchiveBoard() {
        super();
        currentMove = 0;
        capturedPieces = new HashMap<>();
    }

    public ArchiveBoard(List<Move> moves) {
        this();
        this.moves = new Move[moves.size()];
        moves.toArray(this.moves);
    }

    public void calculateState(int moveId) {
        int moveDifference = moveId - currentMove;
        if (moveDifference > 0) {
            goForward(Arrays.copyOfRange(moves, currentMove, moveId));
        } else {
            goBackward(Arrays.copyOfRange(moves, moveId, currentMove));
        }

        currentMove = moveId;
    }

    public void goForward(Move[] moves) {
        for (int i = 0; i < moves.length; i++) {
            Piece piece = tilePieceAssignment.remove(moves[i].getStartTile());
            if (tilePieceAssignment.containsKey(moves[i].getDestinationTile())) {
                Piece capturedPiece = tilePieceAssignment.remove(moves[i].getDestinationTile());
                capturedPieces.put(i + currentMove + 1, capturedPiece);
            }
            tilePieceAssignment.put(moves[i].getDestinationTile(), piece);

        }
    }

    public void goBackward(Move[] moves) {
        for (int i = moves.length -1; i >=0; i--) {
            Piece piece = tilePieceAssignment.remove(moves[i].getDestinationTile());
            int moveId = currentMove - moves.length +1 + i;
            if (capturedPieces.containsKey(moveId)) {
                tilePieceAssignment.put(moves[i].getDestinationTile(), capturedPieces.get(moveId));
            }
            tilePieceAssignment.put(moves[i].getStartTile(), piece);
        }
    }
}
