package com.oshayer.userservice.entites;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Hotel {
    private Long id;
    private String hotelName;
    private String location;
    private String hotelEmail;
    private String about;
}
