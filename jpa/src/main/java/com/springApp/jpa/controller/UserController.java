package com.springApp.jpa.controller;

import com.springApp.jpa.dto.UserDto;
import com.springApp.jpa.entity.User;
import com.springApp.jpa.exception.UserServiceException;
import com.springApp.jpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;

    @PostMapping("/all")
    public ResponseEntity<Iterable<User>> createUsers(@RequestBody List<User> users) {
        Iterable<User> createdUsers = service.saveAll(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsers);
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = service.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        Optional<User> user = service.findUserByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User saveUser = service.addUser(user);
        return ResponseEntity.ok(saveUser);
    }

    @PostMapping("/userDto")
    public ResponseEntity<User> createUserFromDto(@RequestBody UserDto userDto) {
        User user = new User();
        try {
            user = service.createUserFromDto(userDto);
        } catch (UserServiceException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getErrorCode());
        }
        return ResponseEntity.ok(user);
    }

}
