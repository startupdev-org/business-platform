package com.platform.service;

import com.platform.repository.BookingRepository;
import com.platform.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    public Map<String, Object> getBusinessDashboard(UUID businessId) {
        Map<String, Object> dashboard = new HashMap<>();

        Long totalBookings = bookingRepository.countCompletedByBusiness(businessId);
        Double averageRating = reviewRepository.getAverageRatingByBusiness(businessId);

        dashboard.put("totalBookings", totalBookings != null ? totalBookings : 0L);
        dashboard.put("averageRating", averageRating != null ? averageRating : 0.0);
        dashboard.put("totalReviews", reviewRepository.findByBusinessId(businessId).size());

        return dashboard;
    }
}
