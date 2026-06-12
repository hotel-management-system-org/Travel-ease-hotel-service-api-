package com.travel_ease.hotel_system.scheduler;

import com.travel_ease.hotel_system.entity.Room;
import com.travel_ease.hotel_system.entity.RoomInventory;
import com.travel_ease.hotel_system.reposiroty.RoomInventoryRepository;
import com.travel_ease.hotel_system.reposiroty.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryScheduler {

    private final RoomRepository roomRepository;
    private final RoomInventoryRepository roomInventoryRepository;

    @Scheduled(cron = "0 0 1 * * ?")
    public void generateDailyInventoryWindow(){
        log.info("Starting nightly Room Inventory extension job...");

        LocalDate targetFutureDate = LocalDate.now().plusDays(365);

        List<Room> allRooms = roomRepository.findAll();

        if (allRooms.isEmpty()){
            log.info("No rooms found in the system. Skipping inventory generation.");
            return;
        }

        List<RoomInventory> newInventoryRecords = new ArrayList<>();

        for (Room room : allRooms){
            boolean exists = roomInventoryRepository.existsByRoomAndInventoryDate(room, targetFutureDate);

            if (!exists){
                RoomInventory inventory = RoomInventory.builder()
                        .room(room)
                        .roomId(room.getRoomId())
                        .inventoryDate(targetFutureDate)
                        .totalRooms(1)
                        .bookedRooms(0)
                        .build();

                newInventoryRecords.add(inventory);
            }
        }

        if (!newInventoryRecords.isEmpty()){
            roomInventoryRepository.saveAll(newInventoryRecords);
            log.info("Successfully added {} new inventory records for date: {}", newInventoryRecords.size(), targetFutureDate);
        }else {
            log.info("All rooms already have inventory records for date: {}. Nothing to insert.", targetFutureDate);
        }

    }
}


























