package com.AgriTest.dto;

import com.AgriTest.model.Announcement;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String messageBody;
    private Announcement.TargetAudience targetAudience;
    private Announcement.PriorityLevel priorityLevel;
    private Announcement.AnnouncementStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> attachmentUrls;
}