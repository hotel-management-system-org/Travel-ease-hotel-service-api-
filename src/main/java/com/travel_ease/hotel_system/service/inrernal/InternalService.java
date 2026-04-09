package com.travel_ease.hotel_system.service.inrernal;

import com.travel_ease.hotel_system.dto.request.internal.HoldRoomRequestDto;
import com.travel_ease.hotel_system.dto.request.internal.HotelBookingValidationResponse;
import com.travel_ease.hotel_system.dto.response.ResponseHotelDto;
import com.travel_ease.hotel_system.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalService {

    private final RedisTemplate<String, String> redisTemplate;
    @Value("${hotel.room.hold-duration-minutes}")
    private long holdDurationMinutes;
    private final HotelService hotelService;


    public boolean holdRoom(HoldRoomRequestDto request) {
        System.out.println("Hold room request");
        if(isHeld(request.roomId(),request.checkIn(),request.checkOut())){
            log.warn("Room already held, skipping");
            return false;
        }

        String key =  buildHoldKey(
                request.roomId(),
                request.checkIn(),
                request.checkOut()
        );

        String value = "HELD:" + request.quantity();

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(
                        key,
                        value,
                        Duration.ofMinutes(holdDurationMinutes)
                );

        if (Boolean.TRUE.equals(success)) {
            log.info("Room held in Redis | key={} | ttl={}min", key,holdDurationMinutes);
            return true;
        }else {
            log.warn("Room already held | key={}", key);
            return false;
        }
    }


    public boolean releaseHold(HoldRoomRequestDto request) {
        String key = buildHoldKey(request.roomId(), request.checkIn(), request.checkOut());
        Boolean deleted = redisTemplate.delete(key);

        if(Boolean.TRUE.equals(deleted)){
            log.info("Room hold released | key={}", key);
            return true;
        }
        return false;

    }

    public boolean isHeld(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        String key = buildHoldKey(roomId, checkIn, checkOut);
        return redisTemplate.hasKey(key);
    }


    public HotelBookingValidationResponse  validateHotel(String hotelId) throws SQLException {
        ResponseHotelDto response = hotelService.findById(hotelId);
        return HotelBookingValidationResponse.builder()
                .hotelId(response.getHotelId())
                .hotelName(response.getHotelName())
                .status(response.isActiveStatus())
                .build();

    }

    private String buildHoldKey(UUID uuid, LocalDate chickIn, LocalDate checkOut) {
        return String.format("room:hold:%s:%s:%s", uuid, chickIn, checkOut);
    }

}
