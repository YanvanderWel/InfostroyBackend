package com.example.infostroychat.service;

import com.example.infostroychat.domain.User;
import com.example.infostroychat.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean addUser(User user) {
        Optional<User> userFromDB = userRepository.findByName(user.getName());

        if (userFromDB.isPresent()) {
            return false;
        }

        user.setName(user.getName());

        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(User user) {

        if (userRepository.findByName(user.getName()).isPresent()) {
            userRepository.deleteByName(user.getName());
            return true;
        }

        return false;
    }

    public void changeUserHandPosition(User user) {
        Optional<User> userDB = userRepository.findByName(user.getName());
        userDB.ifPresent(value -> {
            value.setHandRaised(!value.isHandRaised());
            userRepository.save(userDB.get());
        });
    }

}

