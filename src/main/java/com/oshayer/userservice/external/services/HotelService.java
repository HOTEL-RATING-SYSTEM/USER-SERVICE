package com.oshayer.userservice.external.services;


import com.oshayer.userservice.entites.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="HotelService" , url = "http://localhost:4020")
public interface HotelService {

    @GetMapping("api/hotels/{hotelId}")
    public Hotel getHotel(@PathVariable("hotelId") String hotelId);



}
