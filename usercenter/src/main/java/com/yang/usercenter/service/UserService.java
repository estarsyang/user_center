package com.yang.usercenter.service;

import com.yang.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author ddayang
* @description 针对表【user】的数据库操作Service
* @createDate 2024-03-25 23:53:33
*/
public interface UserService extends IService<User> {


    /**
     *
     * @param userAccount account
     * @param userPassword password
     * @param checkPassword confirm password
     * @return new user id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User user);

    void userLogout(HttpServletRequest request);
}
