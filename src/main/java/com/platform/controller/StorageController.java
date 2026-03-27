package com.platform.controller;

import com.platform.dto.storage.PresignedUrlResponseDTO;
import com.platform.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Storage", description = "File storage endpoints")
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StorageController {

    private final StorageService storageService;

    @Operation(summary = "Get presigned upload URL", description = "Returns a presigned URL to upload a file directly to Supabase Storage")
    @ApiResponse(responseCode = "200", description = "Presigned URL generated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid file type or name")
    @ApiResponse(responseCode = "401", description = "Not authenticated")
    @PostMapping("/upload-url")
    public ResponseEntity<PresignedUrlResponseDTO> getUploadUrl(
            @RequestParam String fileName,
            @RequestParam String folder) {
        PresignedUrlResponseDTO response = storageService.generatePresignedUrl(fileName, folder);
        return ResponseEntity.ok(response);
    }
}