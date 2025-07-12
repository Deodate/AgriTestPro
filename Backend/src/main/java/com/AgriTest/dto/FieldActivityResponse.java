package com.AgriTest.dto;

import com.AgriTest.model.FieldActivity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FieldActivityResponse {
    private Long id;
    private String activityName;
    private String location;
    private UserDto responsiblePerson;
    private LocalDateTime activityDateTime;
    private String observations;
    private List<AttachmentDto> attachments;
    private FieldActivity.FieldActivityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    public static class UserDto {
        private Long id;
        private String username;
        private String fullName;
    }

    @Data
    public static class AttachmentDto {
        private Long id;
        private String fileName;
        private String filePath;
        private String fileType;
        private LocalDateTime uploadedAt;
    }
}