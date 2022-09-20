package com.gazikel.service;

import com.gazikel.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUserByName() {
        // 1. user在表中存在
        User user1 = userService.getUserByName("郭智昊");
        System.out.println(user1);
        Assert.notNull(user1, "用户为空");

        // 2. user在表中不存在
        User user2 = userService.getUserByName("11223");
        Assert.isNull(user2, "用户不为空");
    }
}
