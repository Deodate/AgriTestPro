package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
<<<<<<< HEAD
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
=======
import java.time.LocalDateTime;
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589

@Data
public class TrialPhaseRequest {
    @NotNull(message = "Test case ID is required")
    private Long testCaseId;

    @NotBlank(message = "Phase name is required")
    private String phaseName;

    @NotNull(message = "Date of phase is required")
    private LocalDateTime dateOfPhase;

    private String observations;
    private String testDataEntry;
<<<<<<< HEAD
    
    // Weather data
    private Double weatherTemperature;
    private Double weatherHumidity;
    private Double weatherRainfall;
    
    private String additionalComments;
    private String status;
    
    // Optional file attachments
    private List<MultipartFile> attachments;
=======
    private Double weatherTemperature;
    private Double weatherHumidity;
    private Double weatherRainfall;
    private String additionalComments;
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
} 