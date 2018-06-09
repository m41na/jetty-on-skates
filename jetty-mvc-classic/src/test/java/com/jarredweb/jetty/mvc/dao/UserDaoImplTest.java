package com.jarredweb.jetty.mvc.dao;

import com.jarredweb.jetty.mvc.model.Login;
import com.jarredweb.jetty.mvc.model.User;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDaoConfig.class, loader = AnnotationConfigContextLoader.class)
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/create-tables.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/populate-data.sql")
})
@TestPropertySource("classpath:config/test-app-dao.properties")
public class UserDaoImplTest {
    
    @Autowired
    public UserDao dao;

    @Test
    public void testValidateUser() {
        Login login = new Login();
        login.setPassword("shirt");
        login.setUsername("user_name1");
        User user = dao.validateUser(login);
        assertEquals(user.getUsername(), login.getUsername());
    }

    @Test
    public void testRetrieveUsers() {
        List<User> users = dao.retrieve(0, 100);
        assertEquals(5, users.size());
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("test_pass");
        user.setFirstname("test_first");
        user.setLastname("test_last");
        user.setEmail("test@email.com");
        user.setPhone("test_address");
        user.setAddress("test_address");
        
        dao.register(user);
    }    
}
