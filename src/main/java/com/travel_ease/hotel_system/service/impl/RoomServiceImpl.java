package com.travel_ease.hotel_system.service.impl;

import com.travel_ease.hotel_system.dto.request.ConfirmBookingRequestDto;
import com.travel_ease.hotel_system.dto.request.RoomRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseRoomDto;
import com.travel_ease.hotel_system.dto.response.paginate.RoomPaginateResponseDto;
import com.travel_ease.hotel_system.entity.Branch;
import com.travel_ease.hotel_system.entity.Hotel;
import com.travel_ease.hotel_system.entity.Room;
import com.travel_ease.hotel_system.entity.RoomInventory;
import com.travel_ease.hotel_system.enums.RoomStatusEnum;
import com.travel_ease.hotel_system.exceptions.EntryNotFoundException;
import com.travel_ease.hotel_system.exceptions.InventoryDataCorruptedException;
import com.travel_ease.hotel_system.reposiroty.BranchRepository;
import com.travel_ease.hotel_system.reposiroty.HotelRepository;
import com.travel_ease.hotel_system.reposiroty.RoomInventoryRepository;
import com.travel_ease.hotel_system.reposiroty.RoomRepository;
import com.travel_ease.hotel_system.service.RoomService;
import com.travel_ease.hotel_system.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    @Value("${hotel.inventory.preview-days}")
    private int inventoryPreviewDays;
    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomInventoryRepository roomInventoryRepository;
    private final Mapper mapper;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    @Transactional
    public void create(RoomRequestDto dto) {
        Branch selectedBranch = branchRepository
                .findById(dto.getBranchId()).orElseThrow(() -> new EntryNotFoundException("Branch not found"));

        if(roomRepository.existsByRoomNumberAndBranch(dto.getRoomNumber(), selectedBranch)){
            throw new RuntimeException(
                    String.format("Room number '%s' already exists in this branch", dto.getRoomNumber())
            );
        }

        Hotel selectedHotel = hotelRepository.findById(selectedBranch.getHotel().getHotelId())
                .orElseThrow(() -> new EntryNotFoundException("Hotel not found"));
        Room room = Room.builder()
                .roomId(UUID.randomUUID().toString())
                .roomNumber(dto.getRoomNumber())
                .type(dto.getRoomType())
                .bedCount(dto.getBedCount())
                .status(RoomStatusEnum.AVAILABLE)
                .branch(selectedBranch)
                .hotel(selectedHotel)
                .price(dto.getPrice())
                .build();

        Room savedRoom = roomRepository.save(room);

        log.info("Generating 365 days of inventory for Room Number: {} in Branch: {}", savedRoom.getRoomNumber(), selectedBranch.getBranchName());

        LocalDate today = LocalDate.now();
        List<RoomInventory> inventoryList = new ArrayList<>();

        for (int i = 0; i < inventoryPreviewDays; i++) {
            RoomInventory inventory = RoomInventory.builder()
                    .room(savedRoom)
                    .roomId(String.valueOf(UUID.fromString(savedRoom.getRoomId())))
                    .inventoryDate(today.plusDays(i))
                    .totalRooms(1)
                    .bookedRooms(0)
                    .build();

            inventoryList.add(inventory);
        }
        roomInventoryRepository.saveAll(inventoryList);

        log.info("Successfully created room and initialized 365 inventory records.");
    }

    @Override
    public void update(RoomRequestDto dto, String roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Room not found with id: %s", roomId)
                ));

        if (dto.getBranchId() != null &&
                !dto.getBranchId().equals(room.getBranch().getBranchId())) {
            Branch newBranch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException(
                            String.format("Branch not found with id: %s", dto.getBranchId())
                    ));


            if (roomRepository.existsByRoomNumberAndBranch(dto.getRoomNumber(), newBranch)) {
                throw new RuntimeException(
                        String.format("Room number '%s' already exists in the target branch", dto.getRoomNumber())
                );
            }

            room.setBranch(newBranch);
        } else {

            if (!dto.getRoomNumber().equals(room.getRoomNumber()) &&
                    roomRepository.existsByRoomNumberAndBranch(dto.getRoomNumber(), room.getBranch())) {
                throw new RuntimeException(
                        String.format("Room number '%s' already exists in this branch", dto.getRoomNumber())
                );
            }
        }

        room.setRoomNumber(dto.getRoomNumber());
        room.setType(dto.getRoomType());
        room.setBedCount(dto.getBedCount());
        room.setPrice(dto.getPrice());
        room.setStatus(dto.getStatus());

        roomRepository.save(room);
    }

    @Override
    public void delete(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Room not found with id: %s", roomId)
                ));

        if (room.getFacilities() != null && !room.getFacilities().isEmpty()) {
            throw new RuntimeException(
                    String.format("Cannot delete room with id: %s. It has associated facilities.", roomId)
            );
        }

        if (room.getRoomImages() != null && !room.getRoomImages().isEmpty()) {
            throw new RuntimeException(
                    String.format("Cannot delete room with id: %s. It has associated images.", roomId)
            );
        }

        roomRepository.deleteById(roomId);
    }

    @Override
    public ResponseRoomDto findById(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Room not found with id: %s", roomId)
                ));

        return mapper.toResponseRoomDto(room);
    }

    @Override
    public RoomPaginateResponseDto findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> roomPage = roomRepository.findAll(pageable);

        List<ResponseRoomDto> dataList = roomPage.getContent().stream()
                .map(mapper::toResponseRoomDto)
                .collect(Collectors.toList());

        return RoomPaginateResponseDto.builder()
                .dataList(dataList)
                .dataCount(roomPage.getTotalElements())
                .build();
    }

    @Override
    public boolean updateInventoryOnConfirmation(ConfirmBookingRequestDto request) {
        LocalDate lockRndDate = request.checkOut().minusDays(1);
        long totalNights = ChronoUnit.DAYS.between(request.checkIn(), lockRndDate);

        List<RoomInventory> inventories = roomInventoryRepository
                .findInventoryForUpdate(UUID.fromString(request.roomId()), request.checkIn(), request.checkOut());

        if (inventories.size() != totalNights) {
            throw new InventoryDataCorruptedException("Inventory records missing!");
        }

        List<String> redisKeysToRemove = new ArrayList<>();

        for (RoomInventory dailyInventory : inventories) {
            if (dailyInventory.getBookedRooms() + request.quantity() > dailyInventory.getTotalRooms()) {
                return false;
            }
            dailyInventory.setBookedRooms(dailyInventory.getBookedRooms() + request.quantity());

            String dailyHoldKey = String.format("room:hold:%s:%s", request.roomId(), dailyInventory.getInventoryDate());
            redisKeysToRemove.add(dailyHoldKey);
        }
        roomInventoryRepository.saveAll(inventories);

        try {
            redisTemplate.delete(redisKeysToRemove);
        } catch (Exception e) {
            log.error("Redis clear failed: {}", e.getMessage());
        }
        return true;
    }
}


























