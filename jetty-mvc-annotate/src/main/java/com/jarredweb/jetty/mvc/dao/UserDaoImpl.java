package com.jarredweb.jetty.mvc.dao;

import com.jarredweb.jetty.mvc.model.Login;
import com.jarredweb.jetty.mvc.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(@Autowired JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User validateUser(Login login) {
        String sql = "select * from tbl_users where username='" + login.getUsername() + "' and password='" + login.getPassword() + "'";
        List<User> users = jdbcTemplate.query(sql, new UserMapper());
        return users.size() > 0 ? users.get(0) : null;
    }

    @Override
    public List<User> retrieve(int start, int size) {
        String sql = "select * from tbl_users limit " + size + " offset " + start;
        List<User> users = jdbcTemplate.query(sql, new UserMapper());
        return users;
    }

    @Override
    public void register(User user) {
        String sql = "insert into tbl_users (username, password, firstname, lastname, address, email, phone) values (?,?,?,?,?,?,?)";
        int rows = jdbcTemplate.update(sql, new Object[]{user.getEmail(), user.getPassword(), user.getFirstname(), user.getLastname(),
            user.getAddress(), user.getEmail(), user.getPhone()});
        assert rows == 1;
    }
}

class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString(("password")));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setAddress(rs.getString("address"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("phone"));
        return user;
    }
}
