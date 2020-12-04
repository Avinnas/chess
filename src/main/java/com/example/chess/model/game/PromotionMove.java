package com.example.chess.model.game;

import com.example.chess.model.dto.MoveDto;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class PromotionMove extends Move{
    @Transient
    char promotedClass;

    public PromotionMove(char promotedClass) {
        this.promotedClass = promotedClass;
    }

    public PromotionMove(MoveDto move, char promotedClass){
        super(move);
        setPromotedClass(promotedClass);
    }
    public PromotionMove(int startTile, int destinationTile, char promotedClass){
        super(startTile, destinationTile);
        setPromotedClass(promotedClass);
    }

    public PromotionMove() {

    }

    @Override
    public void calculateHash(Board board) {
        super.calculateHash(board);
        hash = hash + "=" + promotedClass;
    }

    public char getPromotedClass() {
        return promotedClass;
    }

    public void setPromotedClass(char promotedClass) {
        this.promotedClass = promotedClass;
    }
}
