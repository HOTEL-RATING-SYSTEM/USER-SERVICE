package com.oshayer.userservice.controllers;

import com.oshayer.userservice.entites.User;
import com.oshayer.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    //create
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User newUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

    }
    //ALL user
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> alluser = userService.getAllUser();
        return ResponseEntity.ok(alluser);
    }



    //single user
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId){
        User gotteduser = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(gotteduser);


    }



}
