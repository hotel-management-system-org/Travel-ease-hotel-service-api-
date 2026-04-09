package com.travel_ease.hotel_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelRequestDto {
    private String hotelName;
    private int starRating;
    private String mainImage;
    private String description;
    private BigDecimal startingFrom;
}
