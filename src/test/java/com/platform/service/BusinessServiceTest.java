package com.platform.service;

import com.platform.entity.Business;
import com.platform.repository.BusinessRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.platform.dto.business.BusinessRequestDTO;
import com.platform.dto.business.BusinessResponseDTO;
import com.platform.entity.User;
import com.platform.exception.BusinessException;
import com.platform.exception.ResourceNotFoundException;
import com.platform.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest {

    @InjectMocks
    private BusinessService businessService;

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private ReviewRepository reviewRepository;

    // ----------------------------
    // Helpers
    // ----------------------------

    private User createBusinessAdmin() {
        return User.builder()
                .id(UUID.randomUUID())
                .role(User.UserRole.BUSINESS_ADMIN)
                .build();
    }

    private User createPlatformAdmin() {
        return User.builder()
                .id(UUID.randomUUID())
                .role(User.UserRole.PLATFORM_ADMIN)
                .build();
    }

    private BusinessRequestDTO createRequest() {
        return BusinessRequestDTO.builder()
                .name("Test Business")
                .description("desc")
                .address("address")
                .city("Chisinau")
                .phone("123")
                .website("site")
                .logoUrl("logo")
                .coverImageUrl("cover")
                .build();
    }

    private Business createBusiness(User owner) {
        return Business.builder()
                .id(UUID.randomUUID())
                .name("Test Business")
                .slug("test-business")
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ----------------------------
    // createBusiness
    // ----------------------------

    @Test
    void createBusiness_success() {

        User owner = createBusinessAdmin();
        BusinessRequestDTO dto = createRequest();

        when(businessRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(reviewRepository.getAverageRatingByBusiness(any()))
                .thenReturn(5.0);

        BusinessResponseDTO response =
                businessService.createBusiness(dto);

        assertNotNull(response);
        assertEquals(dto.getName(), response.getName());

        verify(businessRepository).save(any());
    }

    @Test
    void createBusiness_error() {

        User platformAdmin = createPlatformAdmin();
        BusinessRequestDTO dto = createRequest();

        assertThrows(BusinessException.class, () -> businessService.createBusiness(dto));

        verify(businessRepository, never()).save(any());
    }

    // ----------------------------
    // getBusinessById
    // ----------------------------

    @Test
    void getBusinessById_success() {

        User owner = createBusinessAdmin();
        Business business = createBusiness(owner);

        when(businessRepository.findById(business.getId()))
                .thenReturn(Optional.of(business));

        when(reviewRepository.getAverageRatingByBusiness(any()))
                .thenReturn(4.0);

        BusinessResponseDTO dto =
                businessService.getBusinessById(business.getId());

        assertEquals(business.getId(), dto.getId());
    }

    @Test
    void getBusinessById_notFound() {

        UUID id = UUID.randomUUID();

        when(businessRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> businessService.getBusinessById(id)
        );
    }

    // ----------------------------
    // getBusinessBySlug
    // ----------------------------

    @Test
    void getBusinessBySlug_success() {

        User owner = createBusinessAdmin();
        Business business = createBusiness(owner);

        when(businessRepository.findBySlug(business.getSlug()))
                .thenReturn(Optional.of(business));

        when(reviewRepository.getAverageRatingByBusiness(any()))
                .thenReturn(3.5);

        BusinessResponseDTO dto =
                businessService.getBusinessBySlug(business.getSlug());

        assertEquals(business.getSlug(), dto.getSlug());
    }

    // ----------------------------
    // listBusinesses
    // ----------------------------

    @Test
    void listBusinesses_withCity() {

        User owner = createBusinessAdmin();
        List<Business> businesses =
                List.of(createBusiness(owner));

        when(businessRepository.findByCity("Chisinau"))
                .thenReturn(businesses);

        when(reviewRepository.getAverageRatingByBusiness(any()))
                .thenReturn(5.0);

        Page<BusinessResponseDTO> page =
                businessService.listBusinesses(
                        "Chisinau",
                        null,
                        PageRequest.of(0, 10)
                );

        assertEquals(1, page.getContent().size());
    }

    // ----------------------------
    // updateBusiness
    // ----------------------------

    @Test
    void updateBusiness_success_owner() {

        User owner = createBusinessAdmin();
        Business business = createBusiness(owner);

        when(businessRepository.findById(business.getId()))
                .thenReturn(Optional.of(business));

        when(businessRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(reviewRepository.getAverageRatingByBusiness(any()))
                .thenReturn(5.0);

        BusinessResponseDTO dto =
                businessService.updateBusiness(
                        business.getId(),
                        createRequest(),
                        owner
                );

        assertEquals("Test Business", dto.getName());
    }

    @Test
    void updateBusiness_shouldThrow_unauthorized() {

        User owner = createBusinessAdmin();
        Business business = createBusiness(owner);

        User otherUser = createBusinessAdmin();

        when(businessRepository.findById(business.getId()))
                .thenReturn(Optional.of(business));

        assertThrows(
                BusinessException.class,
                () -> businessService.updateBusiness(
                        business.getId(),
                        createRequest(),
                        otherUser
                )
        );
    }

    // ----------------------------
    // deleteBusiness
    // ----------------------------

    @Test
    void deleteBusiness_success_platformAdmin() {

        User owner = createBusinessAdmin();
        Business business = createBusiness(owner);

        User platformAdmin = createPlatformAdmin();

        when(businessRepository.findById(business.getId()))
                .thenReturn(Optional.of(business));

        businessService.deleteBusiness(business.getId(), platformAdmin);

        verify(businessRepository).delete(business);
    }

    // ----------------------------
    // getUserBusinesses
    // ----------------------------

    @Test
    void getUserBusinesses_success() {

        UUID userId = UUID.randomUUID();

        User owner = createBusinessAdmin();
        owner.setId(userId);

        List<Business> businesses = List.of(createBusiness(owner));

        when(businessRepository.findByOwnerId(userId))
                .thenReturn(businesses);

        when(reviewRepository.getAverageRatingByBusiness(any()))
                .thenReturn(4.0);

        List<BusinessResponseDTO> result =
                businessService.getUserBusinesses(userId);

        assertEquals(1, result.size());
    }
}
