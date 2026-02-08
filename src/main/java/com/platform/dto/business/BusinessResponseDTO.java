package com.platform.dto.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessResponseDTO {

    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String address;
    private String city;
    private String phone;
    private String website;
    private String logoUrl;
    private String coverImageUrl;
    private Double ratingOverall;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
