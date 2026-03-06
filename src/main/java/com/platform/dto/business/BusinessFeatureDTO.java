package com.platform.dto.business;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessFeatureDTO {
    private Long featureId;
    private UUID businessId;
    private String name;
}
