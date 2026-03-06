package com.platform.service;

import com.platform.dto.business.BusinessFeatureDTO;
import com.platform.entity.Business;
import com.platform.entity.BusinessFeature;
import com.platform.entity.User;
import com.platform.exception.BusinessException;
import com.platform.exception.BusinessFeatureAlreadyExistsException;
import com.platform.exception.BusinessOwnershipException;
import com.platform.exception.UserNotEnabledException;
import com.platform.repository.BusinessFeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final BusinessFeatureRepository featureRepository;
    private final BusinessService businessService;
    private final UserService userService;

    public List<BusinessFeatureDTO> getAllFeatures(UUID businessId) {
        Business business = businessService.getBusinessById(businessId);

        return featureRepository.findByBusinessId(business.getId())
                .stream()
                .map(f -> BusinessFeatureDTO.builder()
                        .businessId(f.getBusiness().getId())
                        .name(f.getName())
                        .build())
                .toList();
    }

    public BusinessFeatureDTO addFeature(BusinessFeatureDTO request) {
        User user = userService.getUser();

        if (!user.isEnabled())
            throw new UserNotEnabledException("User is not enabled");

        Business business = businessService.getBusinessById(request.getBusinessId());

        if (business.isNotOwner(user)) {
            throw new BusinessOwnershipException("Cannot add a new feature to a business you do not own");
        }

        if(featureRepository.existsByBusinessIdAndName(request.getBusinessId(), request.getName())) {
            throw new BusinessFeatureAlreadyExistsException(
                    "Feature already exists for this business"
            );
        }

        BusinessFeature feature = BusinessFeature.builder()
                .business(business)
                .name(request.getName())
                .build();

        featureRepository.save(feature);

        return BusinessFeatureDTO.builder()
                .featureId(feature.getFeatureId())
                .businessId(feature.getBusiness().getId())
                .name(feature.getName())
                .build();

    }

    public void removeFeature(UUID businessId, Long featureId) {
        Business business = businessService.getBusinessById(businessId);

        if (!Business.hasFeatureById(business, featureId)) {
            throw new BusinessException("Cannot remove a feature from another business");
        }

        BusinessFeature feature = getFeatureById(featureId);
        featureRepository.delete(feature);
    }

    public BusinessFeature getFeatureById(Long featureId) {
        return featureRepository.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature not found"));
    }
}
