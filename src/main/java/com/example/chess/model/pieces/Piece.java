package com.example.chess.model.pieces;


import com.example.chess.model.game.Board;
import com.example.chess.model.game.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece implements Cloneable{

    protected final String name;
    protected int tileNumber;
    protected Color color;

    public Piece(int tileNumber, Color color) {
        this.name = this.getClass().getSimpleName();
        this.tileNumber = tileNumber;
        this.color = color;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "tileNumber=" + tileNumber +
                ", color=" + color +
                '}';
    }

    @Override
    public Piece clone() {
        try{
            return (Piece) super.clone();
        } catch (CloneNotSupportedException e){
            throw new AssertionError();
        }

    }

    public abstract List<Integer> findPossibleMoves(Board board);

    public int getTileNumber() {
        return tileNumber;
    }

    public void setTileNumber(int tileNumber) {
        this.tileNumber = tileNumber;
    }

    public Color getColor() {
        return color;
    }

    public List<Integer> searchForMovesInDirection(int x, int y, Board board){

        if (x==0 && y==0) return new ArrayList<>();

        int tileSearched = tileNumber + x*8 +y;
        int tileSearchedColumn = tileSearched % 8;
        List<Integer> possibleMoves = new ArrayList<>();

        while (tileSearched >=0 && tileSearched <=63 && (tileSearchedColumn!=0 || y <0) && (tileSearchedColumn!=7 || y>0)){
            if(board.tileIsOccupiedByOpponent(tileSearched, color)){
                possibleMoves.add(tileSearched);
                break;
            }
            if(!board.tileIsEmpty(tileSearched)) break;

            possibleMoves.add(tileSearched);

            tileSearched += x*8 +y;
            tileSearchedColumn = tileSearched % 8;;
        }
        return  possibleMoves;
    }

    public List<Integer> findControlledTiles(Board board){
        return findPossibleMoves(board);
    }

    public String getName() {
        return name;
    }
}
