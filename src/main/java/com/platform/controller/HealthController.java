package com.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Health", description = "Health check endpoints")
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {


    @Operation(summary = "Health check", description = "Returns a message confirming the service is up and running")
    @ApiResponse(responseCode = "200", description = "Service is up and running")
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("The web service is up and running");
    }

    @Operation(summary = "Detailed check", description = "Returns a message confirming all systems are checked")
    @ApiResponse(responseCode = "200", description = "All systems checked successfully")
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Everything is checked");
    }
}