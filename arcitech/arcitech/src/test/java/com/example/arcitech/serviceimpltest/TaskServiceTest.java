package com.example.arcitech.serviceimpltest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.arcitech.exception.ResourceNotFoundException;
import com.example.arcitech.model.Task;
import com.example.arcitech.repository.TaskRepository;
import com.example.arcitech.service.TaskService;

public class TaskServiceTest {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskService taskService = new TaskService();

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateTask() {
		Task task = new Task();
		task.setTitle("Test Task");
		task.setDescription("Description for Test Task");
		task.setDueDate(LocalDate.of(2024, 7, 25));
		task.setPriority(Task.Priority.HIGH);

		when(taskRepository.save(any(Task.class))).thenReturn(task);

		Task savedTask = taskService.createTask(task);

		assertNotNull(savedTask);
		assertEquals("Test Task", savedTask.getTitle());
		assertEquals("Description for Test Task", savedTask.getDescription());
		assertEquals(LocalDate.of(2024, 7, 25), savedTask.getDueDate());
		assertEquals(Task.Priority.HIGH, savedTask.getPriority());

		verify(taskRepository, times(1)).save(any(Task.class));
	}

	@Test
	public void testGetAllTasks() {
		Task task1 = new Task();
		task1.setId(1L);
		task1.setTitle("Task 1");

		Task task2 = new Task();
		task2.setId(2L);
		task2.setTitle("Task 2");

		List<Task> tasks = Arrays.asList(task1, task2);

		when(taskRepository.findAll()).thenReturn(tasks);

		List<Task> fetchedTasks = taskService.getAllTasks();

		assertEquals(2, fetchedTasks.size());
		assertEquals("Task 1", fetchedTasks.get(0).getTitle());
		assertEquals("Task 2", fetchedTasks.get(1).getTitle());

		verify(taskRepository, times(1)).findAll();
	}

	@Test
	public void testGetTaskById() {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Test Task");

		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

		Optional<Task> foundTaskOptional = taskService.getTaskById(1L);
		assertTrue(foundTaskOptional.isPresent());

		Task foundTask = foundTaskOptional.get();
		assertEquals("Test Task", foundTask.getTitle());

		verify(taskRepository, times(1)).findById(1L);
	}

	public Task updateTask(Long id, Task updatedTask) {
		Optional<Task> existingTaskOptional = taskRepository.findById(id);

		if (existingTaskOptional.isPresent()) {
			Task existingTask = existingTaskOptional.get();

			existingTask.setTitle(updatedTask.getTitle());
			existingTask.setDescription(updatedTask.getDescription());
			existingTask.setDueDate(updatedTask.getDueDate());
			existingTask.setPriority(updatedTask.getPriority());

			return taskRepository.save(existingTask);
		} else {
			throw new ResourceNotFoundException("Task not found with id: " + id);
		}
	}

	@Test
	public void testDeleteTask() {
		Task task = new Task();
		task.setId(1L);

		when(taskRepository.existsById(1L)).thenReturn(true);

		assertDoesNotThrow(() -> taskService.deleteTask(1L));

		verify(taskRepository, times(1)).existsById(1L);
		verify(taskRepository, times(1)).deleteById(1L);
	}

	
}
