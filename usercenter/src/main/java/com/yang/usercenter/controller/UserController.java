package com.yang.usercenter.controller;

import com.yang.usercenter.model.domain.User;
import com.yang.usercenter.model.domain.request.UserLoginRquest;
import com.yang.usercenter.model.domain.request.UserRegisterRquest;
import com.yang.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *
     * @param userRegisterRquest
     * @return
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRquest userRegisterRquest){

        if (userRegisterRquest == null){
            return null;
        }

        String userAccount = userRegisterRquest.getUserAccount();
        String userPassword = userRegisterRquest.getUserPassword();
        String checkPassword = userRegisterRquest.getCheckPassword();

        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }

        long id = userService.userRegister(userAccount, userPassword, checkPassword);

        return id;
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRquest userLoginRquest, HttpServletRequest httpServletRequest){
        if (userLoginRquest == null){
            return null;
        }
        String userAccount = userLoginRquest.getUserAccount();
        String userPassword = userLoginRquest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }

        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);

        return user;
    }
}
