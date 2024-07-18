package com.example.arcitech.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.arcitech.model.Task;
import com.example.arcitech.service.TaskService;

public class TaskControllerTest {

	@Mock
	private TaskService taskService;

	@InjectMocks
	private TaskController taskController;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
	}

	@Test
	public void testCreateTask() throws Exception {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Test Task");
		task.setDescription("Test Description");
		task.setDueDate(LocalDate.now().plusDays(1));
		task.setPriority(Task.Priority.HIGH);

		when(taskService.createTask(any(Task.class))).thenReturn(task);

		mockMvc.perform(post("/api/tasks").contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(
				"{\"title\": \"Test Task\", \"description\": \"Test Description\", \"dueDate\": \"2024-07-25\", \"priority\": \"HIGH\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Test Task"));

		verify(taskService, times(1)).createTask(any(Task.class));
	}

	
	@Test
	public void testGetTaskById() throws Exception {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Test Task");

		when(taskService.getTaskById(anyLong())).thenReturn(Optional.of(task));

		mockMvc.perform(get("/api/tasks/1").contentType(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Test Task"));

		verify(taskService, times(1)).getTaskById(anyLong());
	}

	@Test
	public void testUpdateTask() throws Exception {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Test Task");
		task.setDescription("Test Description");
		task.setDueDate(LocalDate.now().plusDays(1));
		task.setPriority(Task.Priority.HIGH);

		Task updatedTask = new Task();
		updatedTask.setId(1L);
		updatedTask.setTitle("Updated Task");
		updatedTask.setDescription("Updated Description");
		updatedTask.setDueDate(LocalDate.of(2024, 7, 26));
		updatedTask.setPriority(Task.Priority.MEDIUM);

		when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(updatedTask);

		mockMvc.perform(put("/api/tasks/1").contentType(MediaType.APPLICATION_JSON).content(
				"{\"title\": \"Updated Task\", \"description\": \"Updated Description\", \"dueDate\": \"2024-07-26\", \"priority\": \"MEDIUM\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Updated Task"))
				.andExpect(jsonPath("$.description").value("Updated Description"))
				.andExpect(jsonPath("$.dueDate[0]").value(2024)).andExpect(jsonPath("$.dueDate[1]").value(7))
				.andExpect(jsonPath("$.dueDate[2]").value(26)).andExpect(jsonPath("$.priority").value("MEDIUM"));

		verify(taskService, times(1)).updateTask(anyLong(), any(Task.class));
	}

	@Test
	public void testDeleteTask() throws Exception {
		doNothing().when(taskService).deleteTask(anyLong());

		mockMvc.perform(delete("/api/tasks/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(taskService, times(1)).deleteTask(anyLong());
	}

}
