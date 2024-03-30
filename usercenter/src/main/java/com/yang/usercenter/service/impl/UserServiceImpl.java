package com.yang.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.usercenter.model.domain.User;
import com.yang.usercenter.service.UserService;
import com.yang.usercenter.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ddayang
 * @description use service
 * @createDate 2024-03-25 23:53:33
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;
    private  static final String SALT = "test";

    private static final String USER_LOGIN_STATE = "user_login_state";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

//        check empty

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }

//        lengh
        if (userAccount.length() < 4) {
            return -1;
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

//        could not exist special signal
        String validPattern = "[a-zA-Z0-9]+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if (!matcher.find()) {
            return -1;
        }

//        userPassword the same as checkPassword
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

//        account could not exist
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);

        long count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }

//        encryptPassword
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

//        insert data
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);

        boolean result = this.save(user);
        if (!result){
            return -1;
        }

        return user.getId();


    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {


        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

//        lengh
        if (userAccount.length() < 4) {
            return null;
        }

        if (userPassword.length() < 8) {
            return null;
        }

//        could not exist special signal
        String validPattern = "[a-zA-Z0-9]+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if (!matcher.find()) {
            return null;
        }

        //        account could not exist
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //        encryptPassword
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

//        user not exist or wrong info.
        if (user == null) {
            log.info("user login in fail, account can't match password");
            return null;
        }

        // user desensitization
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setAvatarUrl(user.getAvatarUrl());

        // record user login session
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }
}




