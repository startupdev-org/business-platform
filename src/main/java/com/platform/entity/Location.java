package com.platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Business business;

    private String name;
    private String address;
    private String city;
    private String country;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private Boolean isDefaultLocation = false;  // true if this is the "virtual" default location

}
