package com.example.chess.model.game;

public class PromotionMove extends Move{
    char promotedClass;

    public PromotionMove(char promotedClass) {
        this.promotedClass = promotedClass;
    }

    public char getPromotedClass() {
        return promotedClass;
    }

    public void setPromotedClass(char promotedClass) {
        this.promotedClass = promotedClass;
    }
}
