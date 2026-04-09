package com.travel_ease.hotel_system.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseHotelFacilityDto {
  private Long id;
  private String name;
  private String icon;
  private String hotelId;
}
