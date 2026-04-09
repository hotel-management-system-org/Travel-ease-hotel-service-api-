package com.travel_ease.hotel_system.reposiroty;

import com.travel_ease.hotel_system.entity.Branch;
import com.travel_ease.hotel_system.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,String> {
    boolean existsByRoomNumberAndBranch(String roomNumber, Branch selectedBranch);

    @Query("""
   SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId
""")
    Optional<List<Room>> getRoomsByHotel(String hotelId);

}
