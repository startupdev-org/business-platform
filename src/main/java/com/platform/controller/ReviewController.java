package com.platform.controller;

import com.platform.dto.review.ReviewRequestDTO;
import com.platform.dto.review.ReviewResponseDTO;
import com.platform.entity.User;
import com.platform.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Tag(name = "Review", description = "Review management endpoints")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/booking/{bookingId}")
    @Operation(summary = "Add review to completed booking")
    public ResponseEntity<ReviewResponseDTO> createReview(
            @PathVariable UUID bookingId,
            @Valid @RequestBody ReviewRequestDTO request) {
        ReviewResponseDTO review = reviewService.createReview(bookingId, request);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID")
    public ResponseEntity<ReviewResponseDTO> getReview(@PathVariable UUID id) {
        ReviewResponseDTO review = reviewService.getReview(id);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/business/{businessId}")
    @Operation(summary = "List business reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getBusinessReviews(@PathVariable UUID businessId) {
        List<ReviewResponseDTO> reviews = reviewService.getBusinessReviews(businessId);
        return ResponseEntity.ok(reviews);
    }

    @PatchMapping("/{id}/reply")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add business reply to review")
    public ResponseEntity<ReviewResponseDTO> addBusinessReply(
            @PathVariable UUID id,
            @RequestParam String reply,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        ReviewResponseDTO review = reviewService.addBusinessReply(id, reply, currentUser);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/business/{businessId}/average")
    @Operation(summary = "Get business average rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable UUID businessId) {
        Double average = reviewService.getAverageRating(businessId);
        return ResponseEntity.ok(average);
    }
}
