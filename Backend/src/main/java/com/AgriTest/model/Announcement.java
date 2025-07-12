package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "broadcast_announcements")
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String messageBody;

    @Enumerated(EnumType.STRING)
    private TargetAudience targetAudience;

    @Enumerated(EnumType.STRING)
    private PriorityLevel priorityLevel;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "announcement", 
               cascade = CascadeType.ALL, 
               orphanRemoval = true, 
               fetch = FetchType.LAZY)
    private List<AnnouncementAttachment> attachments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private AnnouncementStatus status;

    // Initialize attachments list if null
    public void setAttachments(List<AnnouncementAttachment> attachments) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.clear();
        if (attachments != null) {
            this.attachments.addAll(attachments);
            for (AnnouncementAttachment attachment : this.attachments) {
                attachment.setAnnouncement(this);
            }
        }
    }

    public enum TargetAudience {
        ALL_USERS, 
        ADMIN, 
        TESTER, 
        MANAGER, 
        RESEARCHER
    }

    public enum PriorityLevel {
        NORMAL, 
        URGENT
    }

    public enum AnnouncementStatus {
        DRAFT, 
        PUBLISHED, 
        ARCHIVED
    }
}