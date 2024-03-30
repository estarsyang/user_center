package com.yang.usercenter.service;
import java.util.Date;

import com.yang.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testSave(){

        User user = new User();

        user.setUsername("tom");
        user.setUserAccount("tom123");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("123");

        user.setAvatarUrl("01.jpg");


        boolean resulst = userService.save(user);

        Assertions.assertTrue(resulst);

    }

    @Test
    void userRegister() {

        String userAccount = "ads";
        String userPassword = "12345678";
        String checkPassord = "12345678";

//        long result = userService.userRegister(userAccount, userPassword,checkPassord);
//        Assertions.assertEquals(-1, result);

        userAccount = "estar";

        long result = userService.userRegister(userAccount, userPassword,checkPassord);
        Assertions.assertEquals(0, result);

    }
}