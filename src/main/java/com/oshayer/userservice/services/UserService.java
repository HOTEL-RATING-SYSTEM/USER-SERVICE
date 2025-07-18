package com.oshayer.userservice.services;

import com.oshayer.userservice.entites.User;

import java.util.List;

public interface UserService {

    //create
    User saveUser(User user);

    List<User> getAllUser();

    User getUserById(Long id);


}
