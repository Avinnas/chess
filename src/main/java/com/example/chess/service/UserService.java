package com.example.chess.service;

import com.example.chess.model.game.User;
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

    public User findById(int userId){
        return userRepository.findById(userId).orElseThrow();
    }
    public User getCurrentPlayer(){
        return userRepository.findById(getCurrentPlayerId()).orElseThrow();
        }
    }

