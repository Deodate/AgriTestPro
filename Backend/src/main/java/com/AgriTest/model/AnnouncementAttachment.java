package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcement_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
    private Announcement announcement;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    // Constructors
    public AnnouncementAttachment(String fileName, String fileUrl, Announcement announcement) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.announcement = announcement;
        this.uploadedAt = LocalDateTime.now();
    }

    // Additional method for easier creation
    public static AnnouncementAttachment create(String fileName, String fileUrl, Announcement announcement, Long uploadedBy) {
        AnnouncementAttachment attachment = new AnnouncementAttachment();
        attachment.setFileName(fileName);
        attachment.setFileUrl(fileUrl);
        attachment.setAnnouncement(announcement);
        attachment.setUploadedBy(uploadedBy);
        attachment.setUploadedAt(LocalDateTime.now());
        return attachment;
    }
}