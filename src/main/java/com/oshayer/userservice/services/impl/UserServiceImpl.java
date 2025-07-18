package com.oshayer.userservice.services.impl;

import com.oshayer.userservice.entites.Hotel;
import com.oshayer.userservice.entites.Rating;
import com.oshayer.userservice.entites.User;
import com.oshayer.userservice.repositories.UserRepository;
import com.oshayer.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;


    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);




    @Override
    public User saveUser(User user) {
        User newUser = userRepository.save(user);

        return newUser;

    }

    @Override
    public List<User> getAllUser() {

        List<User> users = userRepository.findAll();
        return users;

    }

    @Override
    public User getUserById(Long id) {
        // Fetch user from DB
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null; // or throw UserNotFoundException
        }

        // ✅ Fetch ratings for this user from rating-service
        List<Rating> ratingsOfUser = restTemplate.exchange(
                "http://localhost:4030/api/ratings/users/" + user.getId(),
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<List<Rating>>() {}
        ).getBody();

        logger.info("Ratings of user: {}", ratingsOfUser);

        // ✅ For each rating, call hotel-service to fetch hotel details
        if (ratingsOfUser != null) {
            ratingsOfUser = ratingsOfUser.stream().map(rating -> {
                // Fetch hotel details by hotelId
                Hotel hotel = restTemplate.getForObject(
                        "http://localhost:4020/api/hotels/" + rating.getHotelId(),
                        Hotel.class
                );
                // Set hotel into the rating object
                rating.setHotel(hotel);
                return rating;
            }).collect(Collectors.toList());
        }

        // ✅ Set ratings (with hotel info) into user
        user.setRatings(ratingsOfUser);

        return user;
    }

}
