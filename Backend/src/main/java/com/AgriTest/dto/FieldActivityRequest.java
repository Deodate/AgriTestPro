package com.AgriTest.dto;

import com.AgriTest.model.FieldActivity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FieldActivityRequest {
    private String activityName;
    private String location;
    private Long responsiblePersonId;
    private LocalDateTime activityDateTime;
    private String observations;
    private List<MultipartFile> attachments;
    private FieldActivity.FieldActivityStatus status;
}