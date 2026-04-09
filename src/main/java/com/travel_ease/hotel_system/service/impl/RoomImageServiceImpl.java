package com.travel_ease.hotel_system.service.impl;

import com.amazonaws.services.accessanalyzer.model.InternalServerException;
import com.travel_ease.hotel_system.dto.request.RoomImageRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseRoomImageDto;
import com.travel_ease.hotel_system.dto.response.paginate.RoomImagePaginateResponseDto;
import com.travel_ease.hotel_system.entity.FileFormatter;
import com.travel_ease.hotel_system.entity.Room;
import com.travel_ease.hotel_system.entity.RoomImage;
import com.travel_ease.hotel_system.exceptions.EntryNotFoundException;
import com.travel_ease.hotel_system.reposiroty.RoomImageRepository;
import com.travel_ease.hotel_system.reposiroty.RoomRepository;
import com.travel_ease.hotel_system.service.FileService;
import com.travel_ease.hotel_system.service.RoomImageService;
import com.travel_ease.hotel_system.util.CommonFileSavedBinaryDataDTO;
import com.travel_ease.hotel_system.util.FileDataExtractor;
import com.travel_ease.hotel_system.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.io.InputStreamReader;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomImageServiceImpl implements RoomImageService {

    @Value("${bucketName}")
    private String bucketName;

    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final FileService fileService;
    private final FileDataExtractor fileDataExtractor;
    private final Mapper mapper;

    @Override
    public void create(RoomImageRequestDto dto) {
        CommonFileSavedBinaryDataDTO resource = null;

        Optional<Room> selectedRoom = roomRepository.findById(dto.getRoomId());
        if (selectedRoom.isEmpty()) {
            throw new EntryNotFoundException("Room not found.");
        }

        try {
            resource = fileService.createResource(
                    dto.getFile(),
                    "room/" + selectedRoom.get().getRoomId() + "/images/",
                    bucketName
            );

            RoomImage roomImage = RoomImage.builder()
                    .fileFormatter(
                            new FileFormatter(fileDataExtractor.blobToByteArray(resource.getFileName()),
                                    fileDataExtractor.blobToByteArray(resource.getResourceUrl()),
                                    resource.getDirectory().getBytes(),
                                    fileDataExtractor.blobToByteArray(resource.getHash()))
                    )
                    .room(selectedRoom.get())
                    .build();

            roomImageRepository.save(roomImage);

        } catch (Exception e) {

            if (resource != null) {
                try {
                    fileService.deleteResource(
                            bucketName,
                            resource.getDirectory(),
                            fileDataExtractor.extractActualFileName(
                                    new InputStreamReader(resource.getFileName().getBinaryStream())
                            )
                    );
                } catch (Exception ex) {

                    System.err.println("Failed to delete resource during rollback: " + ex.getMessage());
                }
            }
            throw new InternalServerException("Failed to create room image: " + e.getMessage());
        }
    }

    @Override
    public void update(RoomImageRequestDto dto, String imageId) {
        CommonFileSavedBinaryDataDTO resource = null;

        // Parse imageId to Long
        long id;
        try {
            id = Long.parseLong(imageId);
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    String.format("Invalid image id format: %s", imageId)
            );
        }

        // Find existing room image
        Optional<RoomImage> selectedImage = roomImageRepository.findById(id);
        if (selectedImage.isEmpty()) {
            throw new EntryNotFoundException("Room image not found.");
        }

        // Validate room exists if changing room
        Room room;
        if (dto.getRoomId() != null &&
                !dto.getRoomId().equals(selectedImage.get().getRoom().getRoomId())) {
            Optional<Room> newRoom = roomRepository.findById(dto.getRoomId());
            if (newRoom.isEmpty()) {
                throw new EntryNotFoundException("Room not found.");
            }
            room = newRoom.get();
        } else {
            room = selectedImage.get().getRoom();
        }

        try {
            // Delete old resource
            try {
                fileService.deleteResource(
                        bucketName,
                        fileDataExtractor.byteArrayToString(selectedImage.get().getFileFormatter().getDirectory()),
                        fileDataExtractor.byteArrayToString(selectedImage.get().getFileFormatter().getFileName())
                );
            } catch (Exception e) {
                throw new InternalServerException("Failed to delete existing image resource");
            }

            // Create new resource
            resource = fileService.createResource(
                    dto.getFile(),
                    "room/" + room.getRoomId() + "/images/",
                    bucketName
            );

            // Update room image entity
            selectedImage.get().getFileFormatter().setDirectory(resource.getDirectory().getBytes());
            selectedImage.get().getFileFormatter().setFileName(fileDataExtractor.blobToByteArray(resource.getFileName()));
            selectedImage.get().getFileFormatter().setHash(fileDataExtractor.blobToByteArray(resource.getHash()));
            selectedImage.get().getFileFormatter().setResourceUrl(fileDataExtractor.blobToByteArray(resource.getResourceUrl()));
            selectedImage.get().setRoom(room);

            roomImageRepository.save(selectedImage.get());

        } catch (Exception e) {
            // Rollback: delete new resource and restore old one if possible
            if (resource != null) {
                try {
                    fileService.deleteResource(
                            bucketName,
                            resource.getDirectory(),
                            fileDataExtractor.extractActualFileName(
                                    new InputStreamReader(resource.getFileName().getBinaryStream())
                            )
                    );
                } catch (Exception ex) {
                    System.err.println("Failed to delete resource during rollback: " + ex.getMessage());
                }
            }
            throw new InternalServerException("Failed to update room image: " + e.getMessage());
        }
    }

    @Override
    public void delete(String imageId) {
        // Parse imageId to Long
        long id;
        try {
            id = Long.parseLong(imageId);
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    String.format("Invalid image id format: %s", imageId)
            );
        }

        // Find existing room image
        Optional<RoomImage> selectedImage = roomImageRepository.findById(id);
        if (selectedImage.isEmpty()) {
            throw new EntryNotFoundException("Room image not found.");
        }

        try {
            // Delete resource from storage
            fileService.deleteResource(
                    bucketName,
                    fileDataExtractor.byteArrayToString(selectedImage.get().getFileFormatter().getDirectory()),
                    fileDataExtractor.byteArrayToString(selectedImage.get().getFileFormatter().getFileName())
            );

            // Delete from database
            roomImageRepository.deleteById(id);

        } catch (Exception e) {
            throw new InternalServerException("Failed to delete room image: " + e.getMessage());
        }
    }

    @Override
    public ResponseRoomImageDto findById(String imageId) {
        // Parse imageId to Long
        long id;
        try {
            id = Long.parseLong(imageId);
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    String.format("Invalid image id format: %s", imageId)
            );
        }

        RoomImage roomImage = roomImageRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Room image not found."));

        return mapper.mapImageToDto(roomImage);
    }

    @Override
    public RoomImagePaginateResponseDto findAll(int page, int size, String roomId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomImage> imagePage;

        if (roomId == null || roomId.trim().isEmpty()) {
            // Get all room images
            imagePage = roomImageRepository.findAll(pageable);
        } else {
            // Validate room exists
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new EntryNotFoundException("Room not found."));

            // Get images for specific room
            imagePage = roomImageRepository.findAllByRoom(room, pageable);
        }

        return mapper.buildPaginateResponse(imagePage);
    }
}
