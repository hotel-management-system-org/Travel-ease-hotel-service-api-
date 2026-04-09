package com.travel_ease.hotel_system.reposiroty;

import com.travel_ease.hotel_system.entity.Facility;
import com.travel_ease.hotel_system.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility,Long> {
    boolean existsByNameAndRoom(String name, Room room);
}
