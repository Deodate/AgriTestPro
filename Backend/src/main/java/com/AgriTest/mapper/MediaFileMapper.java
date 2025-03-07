// File: src/main/java/com/AgriTest/mapper/MediaFileMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.model.MediaFile;
import com.AgriTest.model.TestResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MediaFileMapper {
    
    public MediaFileResponse toDto(MediaFile mediaFile) {
        if (mediaFile == null) {
            return null;
        }
        
        return MediaFileResponse.builder()
                .id(mediaFile.getId())
                .testResultId(mediaFile.getTestResult().getId())
                .fileName(mediaFile.getFileName())
                .fileType(mediaFile.getFileType())
                .filePath(mediaFile.getFilePath())
                .uploadedBy(mediaFile.getUploadedBy())
                .uploadedAt(mediaFile.getUploadedAt())
                .build();
    }
    
    public List<MediaFileResponse> toDtoList(List<MediaFile> mediaFiles) {
        if (mediaFiles == null) {
            return List.of();
        }
        return mediaFiles.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public MediaFile toEntity(MultipartFile file, String filePath, TestResult testResult, Long userId) {
        if (file == null) {
            return null;
        }
        
        MediaFile mediaFile = new MediaFile();
        mediaFile.setTestResult(testResult);
        mediaFile.setFileName(file.getOriginalFilename());
        mediaFile.setFileType(file.getContentType());
        mediaFile.setFilePath(filePath);
        mediaFile.setUploadedBy(userId);
        mediaFile.setUploadedAt(LocalDateTime.now());
        
        return mediaFile;
    }
}