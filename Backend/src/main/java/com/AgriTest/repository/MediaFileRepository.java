package com.AgriTest.repository;

import com.AgriTest.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    List<MediaFile> findByTestResultId(Long testResultId);
    
    List<MediaFile> findByFileType(String fileType);
    
    List<MediaFile> findByUploadedBy(Long userId);
}