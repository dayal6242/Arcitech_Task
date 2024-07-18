package com.example.arcitech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.arcitech.model.Task;
import com.example.arcitech.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@PostMapping
	public Task createTask(@RequestBody Task task) {
		return taskService.createTask(task);
	}

	@GetMapping
	public List<Task> getAllTasks() {
		return taskService.getAllTasks();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
		Task task = taskService.getTaskById(id).orElseThrow();
		return ResponseEntity.ok().body(task);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
		Task updatedTask = taskService.updateTask(id, taskDetails);
		return ResponseEntity.ok(updatedTask);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}
}