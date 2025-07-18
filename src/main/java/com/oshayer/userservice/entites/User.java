package com.oshayer.userservice.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;        // ✅ Add this
import java.util.ArrayList;  // Already imported

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name" , nullable = false, length = 100)
    private String name;

    private String email;
    private String password;
    private String about;

    @Transient
    private List<Rating> ratings = new ArrayList<>();
}
