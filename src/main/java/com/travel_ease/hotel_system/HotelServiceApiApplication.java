package com.travel_ease.hotel_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelServiceApiApplication.class, args);
	}

}
