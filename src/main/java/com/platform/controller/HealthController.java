package com.platform.controller;

import com.platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final AuthService authService;

    @GetMapping()
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("The web service is up and running");
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Everything is checked");
    }

}
