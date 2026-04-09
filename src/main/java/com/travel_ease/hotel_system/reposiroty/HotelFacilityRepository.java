package com.travel_ease.hotel_system.reposiroty;

import com.travel_ease.hotel_system.entity.Hotel;
import com.travel_ease.hotel_system.entity.HotelFacility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelFacilityRepository extends JpaRepository<HotelFacility,Long> {
    boolean existsByNameAndHotel(String name, Hotel hotel);
}
