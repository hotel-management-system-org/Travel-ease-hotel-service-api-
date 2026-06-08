package com.travel_ease.hotel_system.service.inrernal;

import com.travel_ease.hotel_system.dto.request.internal.HoldRoomRequestDto;
import com.travel_ease.hotel_system.dto.request.internal.HotelBookingValidationResponse;
import com.travel_ease.hotel_system.dto.response.ResponseHotelDto;
import com.travel_ease.hotel_system.entity.RoomInventory;
import com.travel_ease.hotel_system.reposiroty.RoomInventoryRepository;
import com.travel_ease.hotel_system.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalService {

    private final RedisTemplate<String, String> redisTemplate;
    @Value("${hotel.room.hold-duration-minutes}")
    private long holdDurationMinutes;
    private final HotelService hotelService;
    private final RoomInventoryRepository roomInventoryRepository;


    public boolean holdRoom(HoldRoomRequestDto request) {
        log.info("Executing DB-backed room hold for room: {}", request.roomId());

        List<RoomInventory> inventories = roomInventoryRepository.findInventoryForUpdate(
                request.roomId(),
                request.checkIn(),
                request.checkOut()
        );
        long totalNights = ChronoUnit.DAYS.between(request.checkIn(),request.checkOut());

        if (inventories.size() != totalNights) {
            log.error("Inventory records missing in database for requested dates!");
            return false;
        }

        for (RoomInventory dailyInventory : inventories){
            String dailyHoldKey = String.format("room:hold:%s:%s", request.roomId(), dailyInventory.getInventoryDate());
            String currentHeldStr = redisTemplate.opsForValue().get(dailyHoldKey);
            int currentHeldQty = (currentHeldStr != null) ? Integer.parseInt(currentHeldStr) : 0;

            int actualAvailableRooms = dailyInventory.getTotalRooms() - (dailyInventory.getBookedRooms() + currentHeldQty);

            if (actualAvailableRooms < request.quantity()) {
                log.warn("Not enough rooms available for date: {}. Available: {}", dailyInventory.getInventoryDate(), actualAvailableRooms);
                return false;
            }
        }

        for (RoomInventory dailyInventory : inventories){
            String dailyHoldKey = String.format("room:hold:%s:%s", request.roomId(), dailyInventory.getInventoryDate());
            String currentHeldStr = redisTemplate.opsForValue().get(dailyHoldKey);
            int currentHeldQty = (currentHeldStr != null) ? Integer.parseInt(currentHeldStr) : 0;
            int newHeldQty = currentHeldQty + request.quantity();

            redisTemplate.opsForValue().set(
                    dailyHoldKey,
                    String.valueOf(newHeldQty),
                    Duration.ofMinutes(holdDurationMinutes)
            );
        }

        log.info("Hybrid Room Hold SUCCESS | Room: {} | Duration: {} min", request.roomId(), holdDurationMinutes);
        return true;
    }


    public boolean releaseHold(HoldRoomRequestDto request) {
        String key = buildHoldKey(request.roomId(), request.checkIn(), request.checkOut());
        Boolean deleted = redisTemplate.delete(key);

        if(deleted){
            log.info("Room hold released | key={}", key);
            return true;
        }
        return false;

    }

    public HotelBookingValidationResponse  validateHotel(String hotelId) throws SQLException {
        ResponseHotelDto response = hotelService.findById(hotelId);
        return HotelBookingValidationResponse.builder()
                .hotelId(response.getHotelId())
                .hotelName(response.getHotelName())
                .status(response.isActiveStatus())
                .build();

    }

    private String buildHoldKey(UUID roomId, LocalDate chickIn, LocalDate checkOut) {
        return String.format("room:hold:%s:%s:%s", roomId, chickIn, checkOut);
    }

}
