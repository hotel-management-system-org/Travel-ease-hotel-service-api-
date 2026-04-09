package com.travel_ease.hotel_system.reposiroty;

import com.travel_ease.hotel_system.entity.Room;
import com.travel_ease.hotel_system.entity.RoomImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImage,Long> {

    Page<RoomImage> findAllByRoom(Room room, Pageable pageable);

}
