package com.AgriTest.repository;

import com.AgriTest.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByTargetAudience(Announcement.TargetAudience targetAudience);
    List<Announcement> findByPriorityLevel(Announcement.PriorityLevel priorityLevel);
    List<Announcement> findByStatus(Announcement.AnnouncementStatus status);
}