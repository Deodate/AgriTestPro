package com.AgriTest.dto;

import com.AgriTest.model.Announcement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class AnnouncementRequest {
    @NotBlank(message = "Announcement title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Message body is required")
    @Size(max = 5000, message = "Message body must be less than 5000 characters")
    private String messageBody;

    private Announcement.TargetAudience targetAudience;

    private Announcement.PriorityLevel priorityLevel;

    private List<MultipartFile> attachments;
}