package com.travel_ease.hotel_system.reposiroty;

import com.travel_ease.hotel_system.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel,String> {

    @Query("""
          SELECT COUNT(*) FROM Hotel h WHERE h.hotelName LIKE %?1% AND h.activeStatus=true
""")
    long countAllHotels(String searchText);

    @Query("""
          SELECT h FROM Hotel h WHERE h.hotelName LIKE %?1% AND h.activeStatus=true
""")
    Page<Hotel> searchAllHotels(String searchText,Pageable pageable);

    Optional<Hotel> findByHotelName(String hotelName);
}
