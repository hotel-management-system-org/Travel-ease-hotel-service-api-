package com.travel_ease.hotel_system.reposiroty;

import com.travel_ease.hotel_system.entity.Room;
import com.travel_ease.hotel_system.entity.RoomInventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RoomInventoryRepository extends JpaRepository<RoomInventory, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
             """
                SELECT r FROM RoomInventory r WHERE r.roomId = :roomId AND r.inventoryDate BETWEEN :checkIn AND :checkOut
             """
    )
   List<RoomInventory> findInventoryForUpdate(@Param("roomId") UUID roomId, @Param("checkIn")LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

    boolean existsByRoomAndInventoryDate(Room room, LocalDate targetFutureDate);
}
