package com.oshayer.userservice.configs;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableCaching
public class Myconfig {
    @Bean
    @LoadBalanced


    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
