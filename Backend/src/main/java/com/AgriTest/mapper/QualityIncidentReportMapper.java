package com.AgriTest.mapper;

import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.dto.QualityIncidentReportRequest;
import com.AgriTest.dto.QualityIncidentReportResponse;
import com.AgriTest.model.MediaFile;
import com.AgriTest.model.Product;
import com.AgriTest.model.QualityIncidentReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QualityIncidentReportMapper {
    
    @Autowired
    private MediaFileMapper mediaFileMapper;
    
    public QualityIncidentReport toEntity(QualityIncidentReportRequest request, Product product) {
        QualityIncidentReport report = new QualityIncidentReport();
        report.setIncidentId(request.getIncidentId());
        report.setProduct(product);
        report.setIncidentDate(request.getIncidentDate());
        report.setDescription(request.getDescription());
        report.setCorrectiveActions(request.getCorrectiveActions());
        report.setStatus(request.getStatus());
        return report;
    }
    
    public QualityIncidentReportResponse toDto(QualityIncidentReport report) {
        QualityIncidentReportResponse response = new QualityIncidentReportResponse();
        response.setId(report.getId());
        response.setIncidentId(report.getIncidentId());
        response.setProductId(report.getProduct().getId());
        response.setProductName(report.getProduct().getName());
        response.setIncidentDate(report.getIncidentDate());
        response.setDescription(report.getDescription());
        response.setMediaFiles(mapMediaFiles(report.getMediaFiles()));
        response.setCorrectiveActions(report.getCorrectiveActions());
        response.setStatus(report.getStatus());
        response.setCreatedAt(report.getCreatedAt());
        response.setUpdatedAt(report.getUpdatedAt());
        return response;
    }
    
    public void updateEntityFromRequest(QualityIncidentReport report, QualityIncidentReportRequest request, Product product) {
        report.setIncidentId(request.getIncidentId());
        report.setProduct(product);
        report.setIncidentDate(request.getIncidentDate());
        report.setDescription(request.getDescription());
        report.setCorrectiveActions(request.getCorrectiveActions());
        report.setStatus(request.getStatus());
    }
    
    private List<MediaFileResponse> mapMediaFiles(List<MediaFile> mediaFiles) {
        return mediaFiles.stream()
                .map(mediaFileMapper::toDto)
                .collect(Collectors.toList());
    }
}