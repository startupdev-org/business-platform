package com.platform.dto.location;

import lombok.Data;

import java.util.UUID;

@Data
public class LocationRequestDTO {
    private UUID businessId;
    private String name;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private Boolean isDefaultLocation;
}
