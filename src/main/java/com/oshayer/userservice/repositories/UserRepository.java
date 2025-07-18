package com.oshayer.userservice.repositories;

import com.oshayer.userservice.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {




}
