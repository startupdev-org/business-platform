package com.platform.service;

import com.platform.dto.storage.PresignedUrlResponseDTO;
import com.platform.exception.BusinessException;
import com.platform.exception.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    private final RestTemplate restTemplate;

    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp");
    private static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5MB

    public PresignedUrlResponseDTO generatePresignedUrl(String fileName, String folder) {
        validateFileName(fileName);

        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        String filePath = folder + "/" + uniqueFileName;

        String signedUrl = createSignedUrl(filePath);
        String publicUrl = buildPublicUrl(filePath);

        return PresignedUrlResponseDTO.builder()
                .uploadUrl(signedUrl)
                .publicUrl(publicUrl)
                .filePath(filePath)
                .build();
    }

    private String createSignedUrl(String filePath) {
        String url = supabaseUrl + "/storage/v1/object/upload/sign/" + bucket + "/" + filePath;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("expiresIn", 60); // 60 seconds to upload

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, Map.class);
            String signedUrl = (String) response.getBody().get("url");
            return supabaseUrl + "/storage/v1" + signedUrl;
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for path: {}", filePath, e);
            throw new StorageException("Failed to generate upload URL");
        }
    }

    private String buildPublicUrl(String filePath) {
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + filePath;
    }

    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new BusinessException("File name cannot be empty");
        }

        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessException("File type not allowed. Allowed types: JPG, PNG, WebP");
        }
    }

    public void deleteFile(String fileUrl) {
        String filePath = fileUrl.substring(fileUrl.indexOf(bucket) + bucket.length() + 1);
        String url = supabaseUrl + "/storage/v1/object/" + bucket + "/" + filePath;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
            log.info("Deleted file: {}", filePath);
        } catch (Exception e) {
            log.error("Failed to delete file: {}", filePath, e);
            throw new BusinessException("Failed to delete file");
        }
    }
}