package com.travel_ease.hotel_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseHotelDto {
    private String hotelId;
    private boolean activeStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private String hotelName;
    private String mainImage;
    private int starRating;
    private BigDecimal startingFrom;
    private List<ResponseBranchDto> branches;
    private List<ResponseRoomDto> rooms;
    private List<ResponseHotelFacilityDto> hotelFacilities;
}
