package com.travel_ease.hotel_system.service.impl;

import com.travel_ease.hotel_system.dto.request.HotelRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseHotelDto;
import com.travel_ease.hotel_system.dto.response.paginate.HotelPaginateResponseDto;
import com.travel_ease.hotel_system.entity.Hotel;
import com.travel_ease.hotel_system.exceptions.AlreadyExistsException;
import com.travel_ease.hotel_system.exceptions.EntryNotFoundException;
import com.travel_ease.hotel_system.reposiroty.HotelRepository;
import com.travel_ease.hotel_system.service.HotelService;
import com.travel_ease.hotel_system.util.ByteCodeHandler;
import com.travel_ease.hotel_system.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final Mapper mapper;
    private final ByteCodeHandler byteCodeHandler;


    @Override
    public void create(HotelRequestDto dto) {
        try {
            Optional<Hotel> byHotelName = hotelRepository.findByHotelName(dto.getHotelName());

            if (byHotelName.isPresent()) {
                throw new AlreadyExistsException("Hotel already exists");
            }

            Hotel hotel = mapper.toHotel(dto);
            hotelRepository.save(hotel);
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void update(HotelRequestDto dto, String id) throws SQLException {
        Hotel selectedHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Hotel with id: " + id + " not found"));
        selectedHotel.setHotelName(dto.getHotelName());
        selectedHotel.setUpdatedAt(LocalDateTime.now());
        selectedHotel.setDescription(byteCodeHandler.stringToBlob(dto.getDescription()));
        selectedHotel.setStartingFrom(dto.getStartingFrom());
        selectedHotel.setStarRating(dto.getStarRating());
        hotelRepository.save(selectedHotel);

    }

    @Override
    public void delete(String id) {
        hotelRepository.findById(id).orElseThrow(()->new EntryNotFoundException("Hotel not found!"));
        hotelRepository.deleteById(id);
    }

    @Override
    public ResponseHotelDto findById(String id) throws SQLException {
        Hotel selectedHotel = hotelRepository.findById(id).orElseThrow(()->new EntryNotFoundException("Hotel not found!"));
        return mapper.toResponseHotelDto(selectedHotel);
    }

    @Override
    public HotelPaginateResponseDto findAll(int page, int size, String searchText) {
      return  HotelPaginateResponseDto.builder()
              .dataCount(hotelRepository.countAllHotels(searchText))
              .dataList(
                      hotelRepository.searchAllHotels(searchText, PageRequest.of(page,size))
                              .stream().map(e-> {
                                  try {
                                      ResponseHotelDto responseHotelDto = mapper.toResponseHotelDto(e);
                                      System.out.println("responseHotelDto: " + responseHotelDto.getRooms());
                                      return responseHotelDto;
                                  } catch (SQLException ex) {
                                      throw new RuntimeException(ex);
                                  }
                              }).collect(Collectors.toList())
              )
              .build();
    }
}
