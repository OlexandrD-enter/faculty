package com.app.faculty.service;

import com.app.faculty.model.User;
import com.app.faculty.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByUserName(@NonNull String username) {
        return userRepository.findByUsername(username);
    }
}