package com.travel_ease.hotel_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePoliciesDto {
    private long id;
    private String policy;
    private String description;
    private String roomId;
}
