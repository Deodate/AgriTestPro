package com.AgriTest.repository;

import com.AgriTest.model.EvidenceUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvidenceUploadRepository extends JpaRepository<EvidenceUpload, Long> {
    // Custom query methods can be added here if needed
} 