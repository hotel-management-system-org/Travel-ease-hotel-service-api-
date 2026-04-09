package com.travel_ease.hotel_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelFacilityRequestDto {
    private String hotelId;
    private String icon;
    private String name;

}
