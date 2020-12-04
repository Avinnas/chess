package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;
import com.example.chess.model.pieces.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.StrictMath.abs;

public class Board {

    HashMap<Integer, Piece> tilePieceAssignment;
    boolean[] castlingRights;
    int gameResult;


    public Board() {

        // Castling
        // 0 - whiteShort
        // 1 - whiteLong
        // 2 - blackShort
        // 3 - blackLong
        castlingRights = new boolean[]{true, true, true, true};
        tilePieceAssignment = new HashMap<>();
        gameResult = 0;

        tilePieceAssignment.put(0, new Rook(0, Color.BLACK));
        tilePieceAssignment.put(1, new Knight(1, Color.BLACK));
        tilePieceAssignment.put(2, new Bishop(2, Color.BLACK));
        tilePieceAssignment.put(3, new Queen(3, Color.BLACK));
        tilePieceAssignment.put(4, new King(4, Color.BLACK));
        tilePieceAssignment.put(5, new Bishop(5, Color.BLACK));
        tilePieceAssignment.put(6, new Knight(6, Color.BLACK));
        tilePieceAssignment.put(7, new Rook(7, Color.BLACK));

        tilePieceAssignment.put(8, new Pawn(8, Color.BLACK));
        tilePieceAssignment.put(9, new Pawn(9, Color.BLACK));
        tilePieceAssignment.put(10, new Pawn(10, Color.BLACK));
        tilePieceAssignment.put(11, new Pawn(11, Color.BLACK));
        tilePieceAssignment.put(12, new Pawn(12, Color.BLACK));
        tilePieceAssignment.put(13, new Pawn(13, Color.BLACK));
        tilePieceAssignment.put(14, new Pawn(14, Color.BLACK));
        tilePieceAssignment.put(15, new Pawn(15, Color.BLACK));

        tilePieceAssignment.put(48, new Pawn(48, Color.WHITE));
        tilePieceAssignment.put(49, new Pawn(49, Color.WHITE));
        tilePieceAssignment.put(50, new Pawn(50, Color.WHITE));
        tilePieceAssignment.put(51, new Pawn(51, Color.WHITE));
        tilePieceAssignment.put(52, new Pawn(52, Color.WHITE));
        tilePieceAssignment.put(53, new Pawn(53, Color.WHITE));
        tilePieceAssignment.put(54, new Pawn(54, Color.WHITE));
        tilePieceAssignment.put(55, new Pawn(55, Color.WHITE));

        tilePieceAssignment.put(56, new Rook(56, Color.WHITE));
        tilePieceAssignment.put(57, new Knight(57, Color.WHITE));
        tilePieceAssignment.put(58, new Bishop(58, Color.WHITE));
        tilePieceAssignment.put(59, new Queen(59, Color.WHITE));
        tilePieceAssignment.put(60, new King(60, Color.WHITE));
        tilePieceAssignment.put(61, new Bishop(61, Color.WHITE));
        tilePieceAssignment.put(62, new Knight(62, Color.WHITE));
        tilePieceAssignment.put(63, new Rook(63, Color.WHITE));


    }

    public Board(Piece[] pieces) {
        this();
        tilePieceAssignment = new HashMap<>();
        for (Piece piece : pieces) {
            tilePieceAssignment.put(piece.getTileNumber(), piece);
        }
    }

    public Board(Board board) {
        tilePieceAssignment = new HashMap<>(board.getTilePieceAssignment());
        castlingRights = board.getCastlingRights().clone();
    }

    public HashMap<Integer, HashSet<Integer>> findPseudoLegalMoves(Color currentColor) {
        HashMap<Integer, HashSet<Integer>> possibleMoves = new HashMap<>();
        for (Piece piece : getPlayerPieces(currentColor)) {
            HashSet<Integer> tiles = piece.findPossibleMoves(this);
            if (!tiles.isEmpty()) {
                possibleMoves.put(piece.getTileNumber(), tiles);
            }
        }
        return possibleMoves;
    }


    public HashMap<Integer, HashSet<Integer>> findCurrentPlayerMoves(Color currentColor) {

        var possibleMoves = findPseudoLegalMoves(currentColor);

        int kingTile = getKingTile(currentColor);
        if (kingTile == -1) {
            System.out.println("No king on board");
        }
        var opponentPieces = getPlayerPieces(currentColor.getOpponentColor());
        var tilesControlled = findTilesControlled(opponentPieces);
        var checkingPieces = findCheckingPieces(getKingTile(currentColor), tilesControlled);
        var pinnedMoves = findPinnedPiecesMoves(opponentPieces, kingTile);

        excludeIllegalPinnedPiecesMoves(pinnedMoves, possibleMoves);
        excludeIllegalKingMoves(possibleMoves, tilesControlled, kingTile);
        possibleMoves = excludeIllegalMovesWhenChecked(possibleMoves, checkingPieces, kingTile);

        if (possibleMoves.isEmpty()) {
            findIfCheckmate(currentColor, !checkingPieces.isEmpty());
        }


        return possibleMoves;
    }

    public void excludeIllegalKingMoves
            (HashMap<Integer, HashSet<Integer>> possibleMoves, HashMap<Integer, HashSet<Integer>> tilesControlled, int kingTile) {
        HashSet<Integer> kingMoves = possibleMoves.get(kingTile);

        if (kingMoves == null)
            return;

        HashSet<Integer> allControlled = tilesControlled.values()
                .stream()
                .reduce((x, y) -> {
                    x.addAll(y);
                    return x;
                }).orElseThrow();

        if (kingMoves.contains(kingTile - 2)) {
            if (allControlled.contains(kingTile) || allControlled.contains(kingTile - 1) || allControlled.contains(kingTile - 2)) {
                kingMoves.remove(kingTile - 2);
            }
        }
        if (kingMoves.contains(kingTile + 2)) {
            if (allControlled.contains(kingTile) || allControlled.contains(kingTile + 1) || allControlled.contains(kingTile + 2)) {
                kingMoves.remove(kingTile + 2);
            }
        }
        kingMoves.removeIf(allControlled::contains);
        if (kingMoves.isEmpty()) {
            possibleMoves.remove(kingTile);
        }
    }

    public HashMap<Integer, HashSet<Integer>> excludeIllegalMovesWhenChecked
            (HashMap<Integer, HashSet<Integer>> possibleMoves, HashSet<Integer> checkingPieces, int kingTile) {

        if (checkingPieces.size() > 1) {
            if (possibleMoves.containsKey(kingTile)) {
                var kingMoves = possibleMoves.get(kingTile);
                possibleMoves = new HashMap<>();
                possibleMoves.put(kingTile, kingMoves);
            } else {
                possibleMoves = new HashMap<>();
            }
        }

        if (checkingPieces.size() == 1) {
            var interruptingTiles = findInterruptingTiles(kingTile, checkingPieces.iterator().next());
            HashMap<Integer, HashSet<Integer>> possibleMovesWhenChecked = new HashMap<>();

            for (Map.Entry<Integer, HashSet<Integer>> entry : possibleMoves.entrySet()) {
                HashSet<Integer> movesForPiece = new HashSet<>();

                if (entry.getKey() == kingTile) {
                    possibleMovesWhenChecked.put(entry.getKey(), entry.getValue());
                    continue;
                }

                for (int tile : entry.getValue()) {
                    if (interruptingTiles.contains(tile)) {
                        movesForPiece.add(tile);
                    }
                }
                if (!movesForPiece.isEmpty()) {
                    possibleMovesWhenChecked.put(entry.getKey(), movesForPiece);
                }
            }
            return possibleMovesWhenChecked;
        }
        return possibleMoves;

    }


    public void excludeIllegalPinnedPiecesMoves
            (HashMap<Integer, HashSet<Integer>> pinnedMoves, HashMap<Integer, HashSet<Integer>> possibleMoves) {

        for (var entry : pinnedMoves.entrySet()) {

            if (possibleMoves.containsKey(entry.getKey())) {
                possibleMoves.get(entry.getKey()).removeIf(tile -> !entry.getValue().contains(tile));
            }

        }
        possibleMoves.entrySet().removeIf(x -> x.getValue().isEmpty());
    }

    public HashMap<Integer, HashSet<Integer>> findPinnedPiecesMoves(List<Piece> opponentPieces, int kingTile) {
        HashMap<Integer, HashSet<Integer>> allPossiblePinnedMoves = new HashMap<>();
        for (Piece p : opponentPieces) {
            if (p.getName().equals("Queen") || p.getName().equals("Bishop")) {
                if (isOnSameDiagonal(p.getTileNumber(), kingTile)) {
                    allPossiblePinnedMoves.putAll(findPinnedPieceMovesIfPinExists(p.getTileNumber(), kingTile, p.getColor()));
                }
            }
            if (p.getName().equals("Queen") || p.getName().equals("Rook")) {
                if (isOnSameLine(p.getTileNumber(), kingTile)) {
                    allPossiblePinnedMoves.putAll(findPinnedPieceMovesIfPinExists(p.getTileNumber(), kingTile, p.getColor()));
                }
            }
        }
        return allPossiblePinnedMoves;
    }

    public HashMap<Integer, HashSet<Integer>>
    findPinnedPieceMovesIfPinExists(int potentialPinningPiece, int kingTile, Color pinningPieceColor) {

        int currentTile = potentialPinningPiece;
        HashSet<Integer> pinnedPieceMoves = new HashSet<>();
        int potentialPinnedPiece = 0;
        HashMap<Integer, HashSet<Integer>> movesMap = new HashMap<>();

        while (currentTile != kingTile) {
            pinnedPieceMoves.add(currentTile);
            currentTile = moveTowardsKing(currentTile, kingTile);
            if (tileIsOccupiedByOpponent(currentTile, pinningPieceColor)) {
                if (potentialPinnedPiece != 0) {
                    if (currentTile == kingTile) {
                        movesMap.put(potentialPinnedPiece, pinnedPieceMoves);
                    }
                }
                potentialPinnedPiece = currentTile;
            }

        }
        return movesMap;
    }

    public int moveTowardsKing(int pieceTile, int kingTile) {
        int xVector = (kingTile % 8) - (pieceTile % 8);
        xVector = xVector != 0 ? xVector / abs(xVector) : xVector;
        int yVector = (kingTile / 8) - (pieceTile / 8);
        yVector = yVector != 0 ? yVector / abs(yVector) : yVector;

        return pieceTile + xVector + yVector * 8;
    }

    public boolean isOnSameLine(int pieceTile, int kingTile) {

        return (pieceTile % 8 == kingTile % 8) || (pieceTile / 8 == kingTile / 8);
    }

    public boolean isOnSameDiagonal(int pieceTile, int kingTile) {

        return abs((pieceTile / 8) - (kingTile / 8)) == abs((pieceTile % 8) - (kingTile % 8));
    }

    public HashSet<Integer> findInterruptingTiles(int kingTile, int pieceTile) {
        HashSet<Integer> interruptingTiles = new HashSet<Integer>();
        if (getPiece(pieceTile).getName().equals("Knight")) {
            interruptingTiles.add(pieceTile);
            return interruptingTiles;
        }

        int currentTile = pieceTile;
        while (currentTile != kingTile) {
            interruptingTiles.add(currentTile);
            currentTile = moveTowardsKing(currentTile, kingTile);


            if (currentTile >= 64 || currentTile < 0) {
                throw new IllegalStateException("find interrupting tile - tile is out of board" + kingTile + " " + pieceTile);
            }
        }
        return interruptingTiles;
    }

    public HashMap<Integer, HashSet<Integer>> findTilesControlled(List<Piece> pieces) {
        HashMap<Integer, HashSet<Integer>> controlledTiles = new HashMap<>();

        for (Piece piece : pieces) {
            HashSet<Integer> tiles = piece.findPossibleMoves(this, true);
            if (!tiles.isEmpty()) {
                controlledTiles.put(piece.getTileNumber(), tiles);
            }
        }
        return controlledTiles;
    }


    public void makeMove(MoveDto moveToMake) {
        makeMove(MoveFactory.getMove(moveToMake, this));
    }

    public void makeCastlingMove(CastlingMove moveToMake) {
        makeDefaultMove(moveToMake);
        makeMove(new Move(moveToMake.getRookStartTile(), moveToMake.getRookDestinationTile()));
    }

    public void makePromotionMove(PromotionMove moveToMake) {
        makeDefaultMove(moveToMake);
        Piece p = tilePieceAssignment.remove(moveToMake.getDestinationTile());
        tilePieceAssignment.put(p.getTileNumber(), new Queen(p.getTileNumber(), p.getColor()));

    }


    public void makeMove(Move moveToMake) {
        switch (moveToMake.getClass().getSimpleName()) {
            case "CastlingMove":
                makeCastlingMove((CastlingMove) moveToMake);
                break;
            case "PromotionMove":
                makePromotionMove((PromotionMove) moveToMake);
                break;
            default:
                makeDefaultMove(moveToMake);
        }
    }

    public void makeDefaultMove(Move moveToMake) {

        checkCastlingRights(moveToMake);

        int destinationTileId = moveToMake.getDestinationTile();
        int actualTileId = moveToMake.getStartTile();

        Piece piece = tilePieceAssignment.remove(actualTileId);

        Piece clonedPiece = piece.clone();
        clonedPiece.setTileNumber(destinationTileId);

        tilePieceAssignment.put(destinationTileId, clonedPiece);
    }

    public void checkCastlingRights(Move moveToMake) {
        Piece p = getPiece(moveToMake.getStartTile());
        if (p.getName().equals("King")) {
            disallowCastling(p.getColor());
        }
        if (p.getName().equals("Rook")) {
            if (moveToMake.getStartTile() == 0 || moveToMake.getStartTile() == 56)
                disallowLongCastling(p.getColor());

            if (moveToMake.getStartTile() == 7 || moveToMake.getStartTile() == 63)
                disallowShortCastling(p.getColor());
        }
    }

    public void calculateState(List<Move> moves) {
        moves.forEach(this::makeMove);
    }

    public HashMap<Integer, Piece> getTilePieceAssignment() {
        return tilePieceAssignment;
    }

    public Piece getPiece(int tileNumber) {
        return tilePieceAssignment.get(tileNumber);
    }

    public void removePiece(int tileNumber) {
        Piece p = tilePieceAssignment.remove(tileNumber);
    }

    public List<Piece> getPlayerPieces(Color color) {
        return tilePieceAssignment.values().stream()
                .filter(x -> x.getColor() == color)
                .collect(Collectors.toList());
    }


    public boolean tileIsEmpty(int tileNumber) {
        return !tilePieceAssignment.containsKey(tileNumber);
    }

    public boolean tileIsOccupiedByOpponent(int tileNumber, Color pieceColor) {
        return !tileIsEmpty(tileNumber) && tilePieceAssignment.get(tileNumber).getColor() != pieceColor;
    }

    public boolean[] getCastlingRights() {
        return castlingRights;
    }

    public boolean shortCastlingAllowed(Color color) {
        return Color.WHITE == color ? castlingRights[0] : castlingRights[2];
    }

    public boolean longCastlingAllowed(Color color) {
        return Color.WHITE == color ? castlingRights[1] : castlingRights[3];
    }

    public void disallowCastling(Color color) {
        if (Color.WHITE == color) {
            castlingRights[0] = false;
            castlingRights[1] = false;
        } else {
            castlingRights[2] = false;
            castlingRights[3] = false;
        }

    }

    public void disallowLongCastling(Color color) {
        if (Color.WHITE == color) {
            castlingRights[1] = false;
        } else {
            castlingRights[3] = false;
        }
    }

    public void disallowShortCastling(Color color) {
        if (Color.WHITE == color) {
            castlingRights[0] = false;
        } else {
            castlingRights[2] = false;
        }
    }

    public HashSet<Integer> findCheckingPieces(int kingTile, HashMap<Integer, HashSet<Integer>> tilesControlled) {
        HashSet<Integer> checkingPieces = new HashSet<>();
        for (Integer pieceTile : tilesControlled.keySet()) {
            if (tilesControlled.get(pieceTile).contains(kingTile)) {
                checkingPieces.add(pieceTile);
            }

        }
        return checkingPieces;
    }

    public boolean isCheck(Color color) {
        return !findCheckingPieces(getKingTile(color), findTilesControlled(getPlayerPieces(color))).isEmpty();
    }


    public int getKingTile(Color color) {
        for (Piece piece : tilePieceAssignment.values()) {
            if (piece.getName().equals("King") && piece.getColor() == color)
                return piece.getTileNumber();
        }
        return -1;
    }

    public void addPiece(Piece piece) {
        tilePieceAssignment.put(piece.getTileNumber(), piece);
    }

    public int checkResult(Color currentColor) {
        var whiteMoves = findCurrentPlayerMoves(Color.WHITE);
        var blackMoves = findCurrentPlayerMoves(Color.BLACK);
        boolean whiteChecked= false, blackChecked = false;

        if (findCheckingPieces(getKingTile(currentColor), findTilesControlled(getPlayerPieces(Color.WHITE))).isEmpty()) {
            whiteChecked = true;
        }
        if (findCheckingPieces(getKingTile(currentColor), findTilesControlled(getPlayerPieces(Color.BLACK))).isEmpty()) {
            blackChecked = true;
        }

        if ((whiteMoves.isEmpty() && currentColor == Color.WHITE)
                || (blackMoves.isEmpty() && currentColor == Color.BLACK)
                || drawByMaterial()) {
            gameResult = 10;
        } else if (whiteMoves.isEmpty() && whiteChecked) {
            gameResult = -1;
        } else if (blackMoves.isEmpty() && blackChecked) {
            gameResult = 1;
        }
        return gameResult;
    }

    public int findIfCheckmate(Color lostColor, boolean isChecked) {
        if (!isChecked || drawByMaterial()) {
            gameResult = 10;
        } else {
            if (lostColor == Color.WHITE) {
                gameResult = -1;
            } else {
                gameResult = 1;
            }
        }

        return gameResult;

    }

    public boolean drawByMaterial() {
        if (tilePieceAssignment.size() == 2) {
            return true;
        }
        if (tilePieceAssignment.size() == 3) {
            for (var e : tilePieceAssignment.entrySet()) {
                if (e.getValue().getName().equals("Knight") || e.getValue().getName().equals("Bishop")) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getGameResult() {
        return gameResult;
    }

    public void setGameResult(int gameResult) {
        this.gameResult = gameResult;
    }
}

