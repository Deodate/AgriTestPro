package com.AgriTest.service.impl;

import com.AgriTest.dto.TaskRequest;
import com.AgriTest.dto.TaskResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.Task;
import com.AgriTest.model.User;
import com.AgriTest.repository.TaskRepository;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.service.TaskService;
import com.AgriTest.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        log.info("Creating new task: {}", request.getTaskName());
        
        // Get the assigned user
        User assignedUser = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedTo()));
        
        // Get the current user as creator
        User currentUser = userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        
        // Create task entity
        Task task = new Task();
        task.setTaskName(request.getTaskName());
        task.setAssignedTo(assignedUser);
        task.setDueDate(request.getDueDate());
        task.setPriorityLevel(request.getPriorityLevel());
        task.setTaskDescription(request.getTaskDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : Task.TaskStatus.PENDING);
        task.setCreatedBy(currentUser);
        
        // Save task
        Task savedTask = taskRepository.save(task);
        log.info("Task created with ID: {}", savedTask.getId());
        
        // Return response
        return mapToTaskResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) {
        log.info("Updating task with ID: {}", id);
        
        // Find existing task
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        // Get the assigned user
        User assignedUser = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedTo()));
        
        // Update task fields
        existingTask.setTaskName(request.getTaskName());
        existingTask.setAssignedTo(assignedUser);
        existingTask.setDueDate(request.getDueDate());
        existingTask.setPriorityLevel(request.getPriorityLevel());
        existingTask.setTaskDescription(request.getTaskDescription());
        
        if (request.getStatus() != null) {
            existingTask.setStatus(request.getStatus());
        }
        
        // Save updated task
        Task updatedTask = taskRepository.save(existingTask);
        log.info("Task updated with ID: {}", updatedTask.getId());
        
        // Return response
        return mapToTaskResponse(updatedTask);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        log.info("Fetching task with ID: {}", id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
                
        return mapToTaskResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        log.info("Fetching all tasks");
        
        return taskRepository.findAll().stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getTasksByAssignedUser(Long userId) {
        log.info("Fetching tasks assigned to user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                
        return taskRepository.findByAssignedTo(user).stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getTasksByStatus(Task.TaskStatus status) {
        log.info("Fetching tasks with status: {}", status);
        
        return taskRepository.findByStatus(status).stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getTasksByPriority(Task.PriorityLevel priorityLevel) {
        log.info("Fetching tasks with priority level: {}", priorityLevel);
        
        return taskRepository.findByPriorityLevel(priorityLevel).stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        log.info("Deleting task with ID: {}", id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
                
        taskRepository.delete(task);
        log.info("Task with ID: {} deleted successfully", id);
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(Long id, Task.TaskStatus status) {
        log.info("Updating status of task with ID: {} to {}", id, status);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
                
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        
        Task updatedTask = taskRepository.save(task);
        log.info("Status updated for task with ID: {}", id);
        
        return mapToTaskResponse(updatedTask);
    }
    
    // Helper method to map Task to TaskResponse
    private TaskResponse mapToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTaskName(task.getTaskName());
        
        // Map assigned user
        TaskResponse.UserDto assignedTo = new TaskResponse.UserDto();
        assignedTo.setId(task.getAssignedTo().getId());
        assignedTo.setUsername(task.getAssignedTo().getUsername());
        assignedTo.setFullName(task.getAssignedTo().getFullName());
        response.setAssignedTo(assignedTo);
        
        response.setDueDate(task.getDueDate());
        response.setPriorityLevel(task.getPriorityLevel());
        response.setTaskDescription(task.getTaskDescription());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        
        // Map created by user if available
        if (task.getCreatedBy() != null) {
            TaskResponse.UserDto createdBy = new TaskResponse.UserDto();
            createdBy.setId(task.getCreatedBy().getId());
            createdBy.setUsername(task.getCreatedBy().getUsername());
            createdBy.setFullName(task.getCreatedBy().getFullName());
            response.setCreatedBy(createdBy);
        }
        
        return response;
    }
}