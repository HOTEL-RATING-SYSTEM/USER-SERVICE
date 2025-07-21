package com.oshayer.userservice.services.impl;

import com.oshayer.userservice.entites.Hotel;
import com.oshayer.userservice.entites.Rating;
import com.oshayer.userservice.entites.User;
import com.oshayer.userservice.repositories.UserRepository;
import com.oshayer.userservice.services.UserService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    private static final String RATING_SERVICE_CB = "ratingServiceCB";



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
    @Cacheable(value = "users", key = "#id")
    @CircuitBreaker(name = RATING_SERVICE_CB,fallbackMethod = "getUserByIdFallback")
    @RateLimiter(name = "userServiceRateLimiter", fallbackMethod = "rateLimiterFallback")

//    @Bulkhead(name = "ratingServiceBH", type = Bulkhead.Type.THREADPOOL)

    public User getUserById(Long id) {
        // Fetch user from DB
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null; // or throw UserNotFoundException
        }

        // ✅ Fetch ratings for this user from rating-service
        List<Rating> ratingsOfUser = restTemplate.exchange(
                "http://RATINGSERVICE/api/ratings/users/" + user.getId(),
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
                        "http://HOTELSERVICE/api/hotels/" + rating.getHotelId(),
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

    public User getUserByIdFallback(Long id, Throwable throwable) {
        User fallbackUser = new User();
        fallbackUser.setId(id);
        fallbackUser.setName("Fallback User");
        fallbackUser.setEmail("Fallback email");
        fallbackUser.setAbout("Service is temporarrily unavailable");
        fallbackUser.setRatings(new ArrayList<>());
        return fallbackUser;

    }

    // Fallback method when rate limit is exceeded
    public User rateLimiterFallback(Long id, Throwable t) {
        User limited = new User();
        limited.setId(id);
        limited.setName("Rate Limit Exceeded");
        limited.setAbout("Too many requests. Please try again later.");
        return limited;
    }

}
