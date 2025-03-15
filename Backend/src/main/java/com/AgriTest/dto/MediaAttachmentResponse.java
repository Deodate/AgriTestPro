package com.AgriTest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MediaAttachmentResponse {
    
    private Long id;
    private String fileName;
    private String fileType;
    private String filePath;
    private Long fileSize;
    private String description;
    private LocalDateTime uploadedAt;
    private String downloadUrl;
}