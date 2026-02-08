package com.platform.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {

    private UUID id;
    private Double ratingOverall;
    private Double ratingCleanliness;
    private Double ratingService;
    private Double ratingPrice;
    private String comment;
    private String businessReply;
    private UUID bookingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
