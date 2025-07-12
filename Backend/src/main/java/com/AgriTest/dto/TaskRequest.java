package com.AgriTest.dto;

import com.AgriTest.model.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {
    @NotBlank(message = "Task name is required")
    @Size(max = 255, message = "Task name must be less than 255 characters")
    private String taskName;
    
    @NotNull(message = "Assigned user ID is required")
    private Long assignedTo;
    
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be in present or future")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;
    
    private Task.PriorityLevel priorityLevel;
    
    @Size(max = 5000, message = "Task description must be less than 5000 characters")
    private String taskDescription;
    
    private Task.TaskStatus status;
}