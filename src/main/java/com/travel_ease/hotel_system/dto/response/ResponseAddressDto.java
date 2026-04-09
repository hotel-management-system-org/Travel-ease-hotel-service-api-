package com.travel_ease.hotel_system.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseAddressDto {
    private String addressId;
    private String addressLine;
    private String city;
    private String country;
    private BigDecimal longitude;
    private BigDecimal latitude;
}
