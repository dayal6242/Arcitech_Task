package com.example.arcitech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.arcitech.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
