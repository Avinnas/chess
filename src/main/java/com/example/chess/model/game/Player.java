package com.example.chess.model.game;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "players")
public class Player {

    @Id
    int id;
    String name;

}
