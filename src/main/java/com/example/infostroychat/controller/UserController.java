package com.example.infostroychat.controller;

import javax.transaction.Transactional;

import com.example.infostroychat.domain.User;
import com.example.infostroychat.exception.SuchNameAlreadyExistsException;
import com.example.infostroychat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    private final UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/new")
    @SendTo("/topic/chat")
    public ResponseEntity<?> createNewUser(@RequestBody User user) throws SuchNameAlreadyExistsException {

        User userDB = new User(user.getName());

        boolean isUserCreated = userService.addUser(userDB);

        if (isUserCreated) {
            LOG.info("User successfully saved into DB");

            Map<Object, Object> response = new HashMap<>();
            response.put("id", userDB.getId());
            response.put("name", userDB.getName());
            response.put("isHandRaised", userDB.isHandRaised());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            throw new SuchNameAlreadyExistsException("Edit your name.");
        }
    }

    @MessageMapping("/del")
    @SendTo("/topic/logout")
    @Transactional
    public List<User> deleteNewUser(@RequestBody User user) throws SuchNameAlreadyExistsException {

        userService.deleteUser(user);
        return userService.getAllUsers();
    }

    @MessageMapping("/changeHandPosition")
    @SendTo("/topic/hand")
    public List<User> changeUserHandPosition(@RequestBody User user) {

        userService.changeUserHandPosition(user);
        return userService.getAllUsers();

    }

    @GetMapping("all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}