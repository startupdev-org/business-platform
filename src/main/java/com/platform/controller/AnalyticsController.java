package com.platform.controller;

import com.platform.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Analytics", description = "Analytics and dashboard endpoints")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/business/{businessId}/dashboard")
    @Operation(summary = "Get business dashboard analytics")
    public ResponseEntity<Map<String, Object>> getBusinessDashboard(@PathVariable UUID businessId) {
        Map<String, Object> dashboard = analyticsService.getBusinessDashboard(businessId);
        return ResponseEntity.ok(dashboard);
    }
}
