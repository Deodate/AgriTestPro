package com.AgriTest.service;

import com.AgriTest.dto.AnnouncementRequest;
import com.AgriTest.dto.AnnouncementResponse;
import com.AgriTest.model.Announcement;
import java.util.List;

public interface AnnouncementService {
    AnnouncementResponse createAnnouncement(AnnouncementRequest request);
    AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest request);
    AnnouncementResponse getAnnouncementById(Long id);
    List<AnnouncementResponse> getAllAnnouncements();
    void deleteAnnouncement(Long id);
    List<AnnouncementResponse> getAnnouncementsByTargetAudience(Announcement.TargetAudience targetAudience);
    List<AnnouncementResponse> getAnnouncementsByPriorityLevel(Announcement.PriorityLevel priorityLevel);
}