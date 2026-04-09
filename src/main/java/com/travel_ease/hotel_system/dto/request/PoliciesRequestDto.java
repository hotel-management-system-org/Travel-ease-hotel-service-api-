package com.travel_ease.hotel_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PoliciesRequestDto {
    private String policies;
    private String description;
    private String roomId;
}
