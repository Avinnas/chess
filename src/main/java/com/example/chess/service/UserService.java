package com.example.chess.service;

import com.example.chess.model.game.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int getCurrentPlayerId(){
        return 0;
    }
}
