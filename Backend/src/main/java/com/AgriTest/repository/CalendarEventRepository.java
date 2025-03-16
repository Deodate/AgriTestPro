package com.AgriTest.repository;

import com.AgriTest.model.CalendarEvent;
import com.AgriTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    List<CalendarEvent> findByEventType(CalendarEvent.EventType eventType);
    
    List<CalendarEvent> findByCreatedBy(User createdBy);
    
    List<CalendarEvent> findByIsCancelled(Boolean isCancelled);
    
    @Query("SELECT e FROM CalendarEvent e JOIN e.participants p WHERE p.id = :userId")
    List<CalendarEvent> findByParticipantId(Long userId);
    
    List<CalendarEvent> findByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM CalendarEvent e WHERE " +
           "(e.startDateTime >= :start AND e.startDateTime <= :end) OR " +
           "(e.endDateTime >= :start AND e.endDateTime <= :end) OR " +
           "(e.startDateTime <= :start AND e.endDateTime >= :end)")
    List<CalendarEvent> findByDateRange(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM CalendarEvent e JOIN e.participants p WHERE " +
           "p.id = :userId AND ((e.startDateTime >= :start AND e.startDateTime <= :end) OR " +
           "(e.endDateTime >= :start AND e.endDateTime <= :end) OR " +
           "(e.startDateTime <= :start AND e.endDateTime >= :end))")
    List<CalendarEvent> findByParticipantIdAndDateRange(Long userId, LocalDateTime start, LocalDateTime end);
}