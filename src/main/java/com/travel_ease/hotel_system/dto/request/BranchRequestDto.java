package com.travel_ease.hotel_system.dto.request;

import com.travel_ease.hotel_system.enums.BranchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchRequestDto {
    private int roomCount;
    private String branchName;
    private BranchType branchType;
    private String hotelId;
}
