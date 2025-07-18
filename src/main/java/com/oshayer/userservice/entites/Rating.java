package com.oshayer.userservice.entites;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Rating {
    @Id
    private String id;

    private Long userId;

    // Reference to Hotel (from HotelService)
    private Long hotelId;

    private int rating;       // e.g., 1 to 5
    private String feedback;


    private Hotel hotel;
}
