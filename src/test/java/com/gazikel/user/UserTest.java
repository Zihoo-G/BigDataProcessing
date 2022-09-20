package com.gazikel.user;

import com.gazikel.pojo.User;
import com.gazikel.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUserByName() {
        String username = "demo";
        String password = "123456";

        User user = userService.getUserByName(username);

        assertNotNull(user);

        assertEquals(password, user.getPassword());
    }
}
