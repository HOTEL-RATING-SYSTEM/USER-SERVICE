package com.oshayer.userservice.external.services;

import com.oshayer.userservice.entites.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "rating-service", url = "http://localhost:4030")
public interface RatingService {

    // POST: create a rating
    @PostMapping("/api/ratings/create")
    Rating createRating(@RequestBody Rating rating);   //  add @RequestBody

    // PUT: update a rating
    @PutMapping("/api/ratings/update/{ratingId}")
    Rating updateRating(
            @PathVariable("ratingId") Long ratingId,       //  specify variable name
            @RequestBody Rating rating                     //  add @RequestBody
    );

    // DELETE: delete a rating
    @DeleteMapping("/api/ratings/delete/{ratingId}")
    void deleteRating(@PathVariable("ratingId") Long ratingId); //  specify variable name
}
