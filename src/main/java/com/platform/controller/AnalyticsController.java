package com.platform.controller;

import com.platform.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/business/{businessId}/dashboard")
    public ResponseEntity<Map<String, Object>> getBusinessDashboard(@PathVariable UUID businessId) {
        Map<String, Object> dashboard = analyticsService.getBusinessDashboard(businessId);
        return ResponseEntity.ok(dashboard);
    }
}
