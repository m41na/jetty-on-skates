package com.jarredweb.jesty.todos;

import com.jarredweb.jesty.route.AppConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodosDao {

    private static final Logger LOG = LoggerFactory.getLogger(TodosDao.class);
    private static TodosDao todos;
    private final AppConfig config = AppConfig.instance();
    private DataSource ds;

    private TodosDao() {
        initDataSource();
        createTable();
    }

    public static TodosDao instance() {
        if (todos == null) {
            synchronized (TodosDao.class) {
                todos = new TodosDao();
            }
        }
        return todos;
    }

    private void initDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(config.properties().getProperty("jdbc.driverClassName"));
        dataSource.setUrl(config.properties().getProperty("jdbc.url"));
        dataSource.setUsername(config.properties().getProperty("jdbc.username"));
        dataSource.setPassword(config.properties().getProperty("jdbc.password"));
        this.ds = dataSource;
    }

    private void createTable() {
        String[] query = {
            "CREATE TABLE IF NOT EXISTS tbl_todos (",
            "  task varchar(25) UNIQUE NOT NULL,",
            "  completed boolean DEFAULT false,",
            "  date_created datetime default current_timestamp,",
            "  PRIMARY KEY (task)",
            ")"
        };

        try (Connection con = ds.getConnection(); Statement stmt = con.createStatement();) {
            boolean result = stmt.execute(String.join("", query));
            if (result) {
                LOG.info("createTable was successful");
            }
        } catch (SQLException sq) {
            LOG.error(sq.getMessage(), sq);
        }
    }

    public int createTask(String task) {
        String query = "INSERT INTO tbl_todos (task) values (?)";

        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
            ps.setString(1, task);
            int result = ps.executeUpdate();
            LOG.info("createTask was successful");
            return result;
        } catch (SQLException sq) {
            LOG.error(sq.getMessage(), sq);
            return -1;
        }
    }

    public int updateDone(String task, boolean done) {
        String query = "UPDATE tbl_todos set completed=? where task = ?";

        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
            ps.setBoolean(1, done);
            ps.setString(2, task);
            int result = ps.executeUpdate();
            LOG.info("updateDone was successful");
            return result;
        } catch (SQLException sq) {
            LOG.error(sq.getMessage(), sq);
            return -1;
        }
    }

    public int updateName(String task, String newName) {
        String query = "UPDATE tbl_todos set task=? where task = ?";

        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
            ps.setString(1, newName);
            ps.setString(2, task);
            int result = ps.executeUpdate();
            LOG.info("updateName was successful");
            return result;
        } catch (SQLException sq) {
            LOG.error(sq.getMessage(), sq);
            return -1;
        }
    }

    public int deleteTask(String task) {
        String query = "DELETE from tbl_todos where task = ?";

        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
            ps.setString(1, task);
            int result = ps.executeUpdate();
            LOG.info("deleteTask was successful");
            return result;
        } catch (SQLException sq) {
            LOG.error(sq.getMessage(), sq);
            return -1;
        }
    }

    public Task retrieveTask(String name) {
        String query = "SELECT * from tbl_todos where task = ?";

        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
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
        } catch (SQLException sq) {
            LOG.error(sq.getMessage(), sq);
            return null;
        }
    }

    public List<Task> retrieveByRange(int start, int size) {
        String query = "SELECT * from tbl_todos limit ? offset ?";

        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
            ps.setInt(1, size);
            ps.setInt(2, start);
            ResultSet rs = ps.executeQuery();
            List<Task> result = new ArrayList<>();
            while (rs.next()) {
                Task task = new Task();
                task.completed = rs.getBoolean("completed");
                task.name = rs.getString("task");
                task.created = rs.getDate("date_created");
                result.add(task);
            }
            LOG.info("deleteTask was successful");
            return result;
        } catch (SQLException sq) {
            LOG.error(sq.getMessage(), sq);
            return Collections.EMPTY_LIST;
        }
    }

    public List<Task> retrieveByDone(boolean completed) {
        String query = "SELECT * from tbl_todos where completed = ?";

        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
            ps.setBoolean(1, completed);
            ResultSet rs = ps.executeQuery();
            List<Task> result = new ArrayList<>();
            while (rs.next()) {
                Task task = new Task();
                task.completed = rs.getBoolean("completed");
                task.name = rs.getString("task");
                task.created = rs.getDate("date_created");
                result.add(task);
            }
            LOG.info("deleteTask was successful");
            return result;
        } catch (SQLException sq) {
            LOG.error(sq.getMessage(), sq);
            return Collections.EMPTY_LIST;
        }
    }
}
