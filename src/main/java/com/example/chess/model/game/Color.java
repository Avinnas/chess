package com.example.chess.model.game;

public enum Color {
    WHITE(0),
    BLACK(1);
    private int value;

    Color(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
