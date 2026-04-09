package com.travel_ease.hotel_system.entity;

import com.travel_ease.hotel_system.enums.RoomStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="room")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {
    @Id
    @Column(name = "room_id", length = 80, nullable = false)
    private String roomId;

    @Column(name = "room_number", length = 80, nullable = false)
    private String roomNumber;

    @Column(name = "room_type", length = 80, nullable = false)
    private String type;

    @Column(name = "bed_count")
    private int bedCount;

    @Column(name = "check_in")
    private LocalDate checkIn;

    @Column(name = "check_out")
    private LocalDate checkOut;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "is_available")
    @Enumerated(EnumType.STRING)
    private RoomStatusEnum status;

    @ManyToOne()
    @JoinColumn(name="branch_id")
    private Branch branch;

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "room")
    private List<Facility> facilities;

    @OneToMany(mappedBy = "room")
    private List<RoomImage>  roomImages;

    @OneToMany(mappedBy = "room")
    private List<Policies> policies;


}
