package com.AgriTest.service;

import com.AgriTest.dto.TaskRequest;
import com.AgriTest.dto.TaskResponse;
import com.AgriTest.model.Task;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    TaskResponse updateTask(Long id, TaskRequest request);
    TaskResponse getTaskById(Long id);
    List<TaskResponse> getAllTasks();
    List<TaskResponse> getTasksByAssignedUser(Long userId);
    List<TaskResponse> getTasksByStatus(Task.TaskStatus status);
    List<TaskResponse> getTasksByPriority(Task.PriorityLevel priorityLevel);
    void deleteTask(Long id);
    TaskResponse updateTaskStatus(Long id, Task.TaskStatus status);
}