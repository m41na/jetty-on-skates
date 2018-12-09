package com.practicaldime.jesty.todos.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.practicaldime.jesty.todos.Task;

public class TodosDaoSpringTest {

	private TodosDaoSpring dao = TodosDaoSpring.instance();
	
	public TodosDaoSpringTest() {
		//clear tasks
		dao.clearAllTasks();
		//batch insert initial data
		dao.createTasks(Arrays.asList("test 1", "test 2", "test 3", "test 4", "test 5"));
		System.out.println("Initialized table with tests");
	}
	
	@Test
	public void testCreateTask() {
		int result = dao.createTask("testCreateTask");
		assertEquals("Expecting 1", 1, result);
	}
	
	@Test
	public void testCreateTasks() {
		int[] result = dao.createTasks(Arrays.asList("task 1", "task 2", "task 3"));
		for(int i = 0; i < result.length; i++) {
			assertEquals("Expecting 1", 1, result[i]);
		}
	}

	@Test
	public void testUpdateDone() {
		int result = dao.updateDone("test 1", true);
		assertEquals("Expecting 1", 1, result);
		Task task = dao.retrieveTask("test 1");
		assertTrue("Expecting 'true'", task.completed);
	}

	@Test
	public void testUpdateName() {
		int result = dao.updateName("test 1", "name changed");
		assertEquals("Expecting 1", 1, result);
		Task task = dao.retrieveTask("name changed");
		assertEquals("Expecting 'name changed'", "name changed", task.name);
	}

	@Test
	public void testDeleteTask() {
		int result = dao.deleteTask("test 2");
		assertEquals("Expecting 1", 1, result);
		Task task = dao.retrieveTask("test 2");
		assertNull("Expecting 'null'", task);
	}

	@Test
	public void testRetrieveTask() {
		Task task = dao.retrieveTask("test 3");
		assertEquals("Expecting 'test 3'", "test 3", task.name);
	}

	@Test
	public void testRetrieveByRange() {
		List<Task> tasks = dao.retrieveByRange(0, 3);
		assertEquals("Expecting '3'", 3, tasks.size());
	}

	@Test
	public void testRetrieveByDone() {
		int result = dao.updateDone("test 3", true);
		assertEquals("Expecting '1'", 1, result);
		List<Task> tasks = dao.retrieveByDone(true);
		assertEquals("Expecting '1'", 1, tasks.size());
	}
}
