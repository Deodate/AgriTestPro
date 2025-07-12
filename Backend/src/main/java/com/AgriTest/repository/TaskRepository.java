package com.AgriTest.repository;

import com.AgriTest.model.Task;
import com.AgriTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User assignedTo);
    List<Task> findByCreatedBy(User createdBy);
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByPriorityLevel(Task.PriorityLevel priorityLevel);
    List<Task> findByAssignedToAndStatus(User assignedTo, Task.TaskStatus status);
}