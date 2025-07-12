package com.AgriTest.dto;

import com.AgriTest.model.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String taskName;
    private UserDto assignedTo;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;
    
    private Task.PriorityLevel priorityLevel;
    private String taskDescription;
    private Task.TaskStatus status;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private UserDto createdBy;

    @Data
    public static class UserDto {
        private Long id;
        private String username;
        private String fullName;
    }
}