package com.travel_ease.hotel_system.reposiroty;


import com.travel_ease.hotel_system.entity.Policies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PoliciesRepository extends JpaRepository<Policies,Long> {

    @Query("""
     SELECT p FROM Policies p WHERE p.room.roomId = :roomId
""")
    List<Policies> findByRoomId(String roomId);
}
