package com.platform.service;

import com.platform.dto.service.ServiceRequestDTO;
import com.platform.dto.service.ServiceResponseDTO;
import com.platform.entity.Business;
import com.platform.entity.Service;
import com.platform.entity.User;
import com.platform.exception.BusinessException;
import com.platform.exception.ResourceNotFoundException;
import com.platform.repository.BusinessRepository;
import com.platform.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final BusinessRepository businessRepository;

    @Transactional
    public ServiceResponseDTO createService(UUID businessId, ServiceRequestDTO dto, User currentUser) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateBusinessOwnership(business, currentUser);

        Service service = Service.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .durationMinutes(dto.getDurationMinutes())
                .active(true)
                .business(business)
                .build();

        service = serviceRepository.save(service);
        return toDTO(service);
    }

    public ServiceResponseDTO getService(UUID id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        return toDTO(service);
    }

    public List<ServiceResponseDTO> getBusinessServices(UUID businessId) {
        return serviceRepository.findByBusinessId(businessId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceResponseDTO> getActiveServices(UUID businessId) {
        return serviceRepository.findByBusinessIdAndActive(businessId, true)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceResponseDTO updateService(UUID businessId, UUID serviceId, ServiceRequestDTO dto, User currentUser) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateBusinessOwnership(business, currentUser);

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setDurationMinutes(dto.getDurationMinutes());
        if (dto.getActive() != null) {
            service.setActive(dto.getActive());
        }

        service = serviceRepository.save(service);
        return toDTO(service);
    }

    @Transactional
    public void deleteService(UUID businessId, UUID serviceId, User currentUser) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateBusinessOwnership(business, currentUser);

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        serviceRepository.delete(service);
    }

    private void validateBusinessOwnership(Business business, User currentUser) {
        if (!business.getOwner().getId().equals(currentUser.getId()) &&
            !currentUser.getRole().equals(User.UserRole.PLATFORM_ADMIN)) {
            throw new BusinessException("Unauthorized");
        }
    }

    private ServiceResponseDTO toDTO(Service service) {
        return ServiceResponseDTO.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .durationMinutes(service.getDurationMinutes())
                .businessId(service.getBusiness().getId())
                .active(service.getActive())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .build();
    }
}
