package com.travel_ease.hotel_system.reposiroty;

import com.travel_ease.hotel_system.entity.Branch;
import com.travel_ease.hotel_system.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BranchRepository extends JpaRepository<Branch,String> {
    boolean existsByBranchNameAndHotel(String branchName, Hotel selectedHotel);

    Page<Branch> findAllByBranchNameContainingIgnoreCase(String trim, Pageable pageable);

    Page<Branch> findAllByHotel(Pageable pageable, Hotel selectedHotel);

    Page<Branch> findAllByHotelAndBranchNameContainingIgnoreCase(Hotel selectedHotel, String trim, Pageable pageable);
}
