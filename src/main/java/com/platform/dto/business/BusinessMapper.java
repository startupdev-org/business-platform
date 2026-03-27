package com.platform.dto.business;

import com.platform.dto.employee.EmployeeResponseDTO;
import com.platform.dto.service.ServiceResponseDTO;
import com.platform.dto.user.UserResponseDTO;
import com.platform.entity.Business;
import com.platform.entity.BusinessWorkingHours;
import com.platform.entity.ProvidedService;
import com.platform.entity.User;

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


    public static BusinessWorkingHours toEntity(BusinessWorkingHoursDTO dto) {
        BusinessWorkingHours entity = new BusinessWorkingHours();
        entity.setDayOfWeek(dto.getDayOfWeek());
        entity.setOpenTime(dto.getOpenTime());
        entity.setCloseTime(dto.getCloseTime());
        entity.setCloseTime(dto.getCloseTime());
        return entity;
    }

    public static List<BusinessWorkingHoursDTO> toWorkingHoursDTOList(List<BusinessWorkingHours> hours) {
        if (hours == null || hours.isEmpty()) return List.of();
        return hours.stream().map(BusinessMapper::toDTO).toList();
    }

    public static List<BusinessWorkingHours> fromWorkingHoursDTOList(List<BusinessWorkingHoursDTO> hours) {
        if (hours == null || hours.isEmpty()) return List.of();
        return hours.stream().map(BusinessMapper::toEntity).toList();
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

    public static Business toDTO(BusinessResponseDTO businessDTO, Double averageRating, List<ProvidedService> services) {
        return Business.builder()
                .id(businessDTO.getId())
                .name(businessDTO.getName())
                .slug(businessDTO.getSlug())
                .description(businessDTO.getDescription())
                .address(businessDTO.getAddress())
                .city(businessDTO.getCity())
                .phone(businessDTO.getPhone())
                .website(businessDTO.getWebsite())
                .logoUrl(businessDTO.getLogoUrl())
                .coverImageUrl(businessDTO.getCoverImageUrl())
                .ratingOverall(averageRating != null ? averageRating : 0.0)
                .createdAt(businessDTO.getCreatedAt())
                .updatedAt(businessDTO.getUpdatedAt())
                .workingHours(fromWorkingHoursDTOList(businessDTO.getBusinessWorkingHours()))
                .providedServices(services)

                .build();
    }

    public static BusinessResponseDTO toDTO(Business business, Double averageRating, List<ServiceResponseDTO> services, User owner) {
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
                .owner(toDTO(owner))
                .businessWorkingHours(toWorkingHoursDTOList(business.getWorkingHours()))
                .providedServices(services)

                .build();
    }

    public static BusinessResponseDTO toDTO(Business business, Double averageRating, List<ServiceResponseDTO> services, List<EmployeeResponseDTO> empployeeList, User owner) {
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
                .owner(toDTO(owner))
                .employeeList(empployeeList)
                .businessWorkingHours(toWorkingHoursDTOList(business.getWorkingHours()))
                .providedServices(services)

                .build();
    }

    public static BusinessResponseDTO toDTO(Business business, Double averageRating, List<ServiceResponseDTO> services, List<EmployeeResponseDTO> empployeeList, Set<BusinessFeatureDTO> featureList, User owner) {
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
                .owner(toDTO(owner))
                .employeeList(empployeeList)
                .businessWorkingHours(toWorkingHoursDTOList(business.getWorkingHours()))
                .businessFeatures(featureList)
                .providedServices(services)

                .build();
    }

    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
