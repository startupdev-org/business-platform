package com.platform.dto.business;

import com.platform.dto.service.ServiceResponseDTO;
import com.platform.entity.Business;
import com.platform.entity.BusinessWorkingHours;

import java.util.List;
import java.util.Set;

public class BusinessMapper {

    public static BusinessWorkingHoursDTO toDTO(BusinessWorkingHours wh) {
        return BusinessWorkingHoursDTO.builder()
                .id(wh.getId())
                .dayOfWeek(wh.getDayOfWeek())
                .openTime(wh.getOpenTime())
                .closeTime(wh.getCloseTime())
                .build();
    }

    public static List<BusinessWorkingHoursDTO> toWorkingHoursDTOList(List<BusinessWorkingHours> hours) {
        return hours.stream().map(BusinessMapper::toDTO).toList();
    }

    public static BusinessResponseDTO toDTO(Business business, Double averageRating) {
        return BusinessResponseDTO.builder()
                .id(business.getId())
                .name(business.getName())
                .slug(business.getSlug())
                .description(business.getDescription())
                .address(business.getAddress())
                .city(business.getCity())
                .phone(business.getPhone())
                .website(business.getWebsite())
                .logoUrl(business.getLogoUrl())
                .coverImageUrl(business.getCoverImageUrl())
                .ratingOverall(averageRating != null ? averageRating : 0.0)
                .createdAt(business.getCreatedAt())
                .updatedAt(business.getUpdatedAt())
                .businessWorkingHours(toWorkingHoursDTOList(business.getWorkingHours()))
                .build();
    }

    public static BusinessResponseDTO toDTO(Business business, Double averageRating, List<ServiceResponseDTO> services) {
        return BusinessResponseDTO.builder()
                .id(business.getId())
                .name(business.getName())
                .slug(business.getSlug())
                .description(business.getDescription())
                .address(business.getAddress())
                .city(business.getCity())
                .phone(business.getPhone())
                .website(business.getWebsite())
                .logoUrl(business.getLogoUrl())
                .coverImageUrl(business.getCoverImageUrl())
                .ratingOverall(averageRating != null ? averageRating : 0.0)
                .createdAt(business.getCreatedAt())
                .updatedAt(business.getUpdatedAt())
                .businessWorkingHours(toWorkingHoursDTOList(business.getWorkingHours()))
                .providedServices(services)
                .build();
    }

    public static BusinessResponseDTO toDTO(
            Business business,
            Double averageRating,
            List<ServiceResponseDTO> services,
            Set<BusinessFeatureDTO> features
    ) {
        return BusinessResponseDTO.builder()
                .id(business.getId())
                .name(business.getName())
                .slug(business.getSlug())
                .description(business.getDescription())
                .address(business.getAddress())
                .city(business.getCity())
                .phone(business.getPhone())
                .website(business.getWebsite())
                .logoUrl(business.getLogoUrl())
                .coverImageUrl(business.getCoverImageUrl())
                .ratingOverall(averageRating != null ? averageRating : 0.0)
                .createdAt(business.getCreatedAt())
                .updatedAt(business.getUpdatedAt())
                .businessWorkingHours(toWorkingHoursDTOList(business.getWorkingHours()))
                .providedServices(services)
                .businessFeatures(features)
                .build();
    }
}
