package com.practicaldime.jesty.todos.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.practicaldime.jesty.todos.Task;

public class TodosDaoSpring {

	private static final Logger LOG = LoggerFactory.getLogger(TodosDaoSpring.class);
    private static TodosDaoSpring todos;
    private TodosDaoConfig config;

    private TodosDaoSpring() {
        config = new TodosDaoConfig();
        this.createTable();
    }

    public static TodosDaoSpring instance() {
        if (todos == null) {
            synchronized (TodosDaoSpring.class) {
                todos = new TodosDaoSpring();
            }
        }
        return todos;
    }
    
    private void createTable() {
        String[] sql = {
            "CREATE TABLE IF NOT EXISTS tbl_todos (",
            "  task varchar(25) UNIQUE NOT NULL,",
            "  completed boolean DEFAULT false,",
            "  date_created datetime default current_timestamp,",
            "  PRIMARY KEY (task)",
            ")"
        };

        JdbcTemplate jdbc = config.getTemplate().getJdbcTemplate();
        TransactionStatus status  = config.startTransaction(); 
        try {
	        jdbc.execute(String.join("", sql));
            LOG.info("createTable was completed");
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
        }
        config.commitTransaction(status);
    }
    
    public int createTask(String task) {
        String sql = "INSERT INTO tbl_todos (task) values (:task)";
        Map<String, Object> params = new HashMap<>();
        params.put("task", task);
            	
        NamedParameterJdbcTemplate jdbc = config.getTemplate();
        TransactionStatus status  = config.startTransaction(); 
        int result = -1;
        try {
	        result = jdbc.update(sql, params);
            LOG.info("createTask was successful");
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
            return result;
        }
        config.commitTransaction(status);
        return result;
    }
    
    public int[] createTasks(List<String> tasks) {
    	String sql = "INSERT INTO tbl_todos (task) values (?)";
    	
    	NamedParameterJdbcTemplate jdbc = config.getTemplate();
        TransactionStatus status  = config.startTransaction(); 
        int[] result = null;
        try {
        	result = jdbc.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					ps.setString(1, tasks.get(index));
				}
				
				@Override
				public int getBatchSize() {
					return tasks.size();
				}
			});
            LOG.info("createTasks was successful");
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
            return result;
        }
        config.commitTransaction(status);
        return result;
    }

    public int updateDone(String task, boolean done) {
        String sql = "UPDATE tbl_todos set completed=:done where task = :task";
        Map<String, Object> params = new HashMap<>();
        params.put("task", task);
        params.put("done", done);        
        
    	NamedParameterJdbcTemplate jdbc = config.getTemplate();
        TransactionStatus status  = config.startTransaction(); 
        int result = -1;
    	try {
	        result = jdbc.update(sql, params);
            LOG.info("updateDone was successful");
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
            return result;
        }
    	config.commitTransaction(status);
        return result;
    }

    public int updateName(String task, String newName) {
        String sql = "UPDATE tbl_todos set task=:newName where task = :task";
        Map<String, Object> params = new HashMap<>();
        params.put("task", task);
        params.put("newName", newName);
        
        NamedParameterJdbcTemplate jdbc = config.getTemplate();
        TransactionStatus status  = config.startTransaction(); 
        int result = -1;
    	try {
	        result = jdbc.update(sql, params);
            LOG.info("updateName was successful");
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
            return result;
        }
    	config.commitTransaction(status);
    	return result;
    }

    public int deleteTask(String task) {
        String sql = "DELETE from tbl_todos where task = :task";
        Map<String, Object> params = new HashMap<>();
        params.put("task", task);
        
        NamedParameterJdbcTemplate jdbc = config.getTemplate();
        TransactionStatus status  = config.startTransaction(); 
        int result = -1;
    	try {
	        result = jdbc.update(sql, params);
            LOG.info("deleteTask was successful");
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
            return result;
        }
    	config.commitTransaction(status);
    	return result;
    }
    
    public int clearAllTasks() {
		String sql = "TRUNCATE table tbl_todos";

		NamedParameterJdbcTemplate jdbc = config.getTemplate();
        TransactionStatus status  = config.startTransaction(); 
        int result = -1;
    	try {
            result = jdbc.getJdbcTemplate().update(sql);
            LOG.info("clearAllTasks was successful");
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
            return -1;
        }
    	config.commitTransaction(status);
    	return result;
	}

    public Task retrieveTask(String name) {
        String sql = "SELECT * from tbl_todos where task = :name";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        
        NamedParameterJdbcTemplate jdbc = config.getTemplate();
        TransactionStatus status  = config.startTransaction(TransactionDefinition.PROPAGATION_SUPPORTS); 
        Task task = null;
        try {
            task = jdbc.query(sql, params, new ResultSetExtractor<Task>() {
				@Override
				public Task extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
		                Task task = new Task();
		                task.completed = rs.getBoolean("completed");
		                task.name = rs.getString("task");
		                task.created = rs.getDate("date_created");
		                LOG.info("retrieveTask was successful");
		                return task;
		            } else {
		                return null;
		            }
				}            	
			});
            LOG.info("retrieveTask was successful");            
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
            return task;
        }
        config.commitTransaction(status);
        return task;
    }

    public List<Task> retrieveByRange(int start, int size) {
        String sql = "SELECT * from tbl_todos limit :size offset :start";
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("size", size);
        
        NamedParameterJdbcTemplate jdbc = config.getTemplate();
        TransactionStatus status  = config.startTransaction(TransactionDefinition.PROPAGATION_SUPPORTS); 
        List<Task> result = Collections.emptyList();
        try {
            result = jdbc.query(sql, params, new RowMapper<Task>() {

				@Override
				public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
					Task task = new Task();
	                task.completed = rs.getBoolean("completed");
	                task.name = rs.getString("task");
	                task.created = rs.getDate("date_created");
	                return task;
				}            	
			});
            LOG.info("retrieveByRange was successful");
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
            return result;
        }
        config.commitTransaction(status);
        return result;
    }

    public List<Task> retrieveByDone(boolean completed) {
        String sql = "SELECT * from tbl_todos where completed = :completed";
        Map<String, Object> params = new HashMap<>();
        params.put("completed", completed);
        
        NamedParameterJdbcTemplate jdbc = config.getTemplate();
        TransactionStatus status  = config.startTransaction(TransactionDefinition.PROPAGATION_SUPPORTS); 
        List<Task> result = Collections.emptyList();
        try {
            result = jdbc.query(sql, params, new RowMapper<Task>() {

				@Override
				public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
					Task task = new Task();
	                task.completed = rs.getBoolean("completed");
	                task.name = rs.getString("task");
	                task.created = rs.getDate("date_created");
	                return task;
				}            	
			});
            LOG.info("retrieveByDone was successful");
        } catch (Exception sq) {
            LOG.error(sq.getMessage(), sq);
            config.rollbackTransaction(status);
            return result;
        }
        config.commitTransaction(status);
        return result;
    }
}
