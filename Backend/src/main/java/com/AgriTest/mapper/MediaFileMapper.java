package com.AgriTest.mapper;

import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.model.MediaFile;
import com.AgriTest.model.QualityIncidentReport;
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
                .fileName(mediaFile.getFileName())
                .fileType(mediaFile.getFileType())
                .fileDownloadUri(mediaFile.getFilePath())
                .size(mediaFile.getFileSize() != null ? mediaFile.getFileSize() : 0L)
                .uploadedBy(mediaFile.getUploadedBy())
                .testResultId(mediaFile.getTestResult() != null ? mediaFile.getTestResult().getId() : null)
                .incidentReportId(mediaFile.getIncidentReport() != null ? mediaFile.getIncidentReport().getId() : null)
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
        mediaFile.setFileSize(file.getSize());
        mediaFile.setUploadedBy(userId);
        mediaFile.setUploadedAt(LocalDateTime.now());
        
        return mediaFile;
    }
    
    public MediaFile toEntity(MultipartFile file, String filePath, QualityIncidentReport incidentReport, Long userId) {
        if (file == null) {
            return null;
        }
        
        MediaFile mediaFile = new MediaFile();
        mediaFile.setIncidentReport(incidentReport);
        mediaFile.setFileName(file.getOriginalFilename());
        mediaFile.setFileType(file.getContentType());
        mediaFile.setFilePath(filePath);
        mediaFile.setFileSize(file.getSize());
        mediaFile.setUploadedBy(userId);
        mediaFile.setUploadedAt(LocalDateTime.now());
        
        return mediaFile;
    }
}