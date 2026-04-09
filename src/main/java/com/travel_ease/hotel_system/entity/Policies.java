package com.travel_ease.hotel_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="policies")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Policies {
    @Id
    @Column(name = "id", length=80)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "policy")
    private String policy;

    @Column(name = "description",length = 60)
    private String description;

    @ManyToOne()
    @JoinColumn(name = "room_id")
    private Room room;
}
