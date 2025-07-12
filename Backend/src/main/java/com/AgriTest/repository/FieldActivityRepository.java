package com.AgriTest.repository;

import com.AgriTest.model.FieldActivity;
import com.AgriTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FieldActivityRepository extends 
    JpaRepository<FieldActivity, Long>, 
    JpaSpecificationExecutor<FieldActivity> {
    
    List<FieldActivity> findByResponsiblePerson(User responsiblePerson);
    List<FieldActivity> findByStatus(FieldActivity.FieldActivityStatus status);
    List<FieldActivity> findByActivityDateTimeBetween(
        LocalDateTime startDateTime, 
        LocalDateTime endDateTime
    );
}