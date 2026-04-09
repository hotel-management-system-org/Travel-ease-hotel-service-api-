package com.travel_ease.hotel_system.dto.request.internal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelBookingValidationResponse {
    private String hotelId;
    private String hotelName;
    private boolean status;
    private boolean bookingAllowed;

}
