package com.travel_ease.hotel_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="hotel_facility")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HotelFacility {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(length=100, nullable=false, name = "name")
    private String name;

    @Column(length=100, nullable=false, name = "icon")
    private String icon;

    @ManyToOne()
    @JoinColumn(name="hotel_id")
    private Hotel hotel;

}
