package com.example.infostroychat.controller;

import javax.transaction.Transactional;

import com.example.infostroychat.domain.User;
import com.example.infostroychat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    private final UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/joinTheGroup")
    @SendTo("/topic/chat")
    public List<User> joinTheGroup() {
        LOG.info("User has joined the group");
        return userService.getAllUsers();
    }

    @PostMapping("/new")
    public ResponseEntity<?> createNewUser(@RequestBody User user) {

        User userDB = new User(user.getName());

        boolean isUserCreated = userService.addUser(userDB);

        if (isUserCreated) {
            LOG.info("User has been successfully saved into DB");

            return ResponseEntity.status(HttpStatus.OK).body("User was added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Edit your name.");
        }
    }

    @MessageMapping("/del")
    @SendTo("/topic/logout")
    @Transactional
    public List<User> deleteNewUser(@Payload User user) {

        boolean isUserDeleted = userService.deleteUser(user);

        if (isUserDeleted) {
            LOG.info("User has been successfully deleted from DB");

        } else {
            LOG.info("The user has not been deleted from the database.");
        }
        return userService.getAllUsers();

    }

    @MessageMapping("/changeHandPosition")
    @SendTo("/topic/hand")
    @Transactional
    public List<User> changeUserHandPosition(@Payload User user) {

        userService.changeUserHandPosition(user);

        LOG.info("User " + user.getName() + " has raised hand");
        return userService.getAllUsers();

    }

    @GetMapping("all")
    public List<User> getAllUsers() {
        LOG.info("All users have been gotten from DB");
        return userService.getAllUsers();
    }

}
