package com.yang.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.usercenter.common.BaseResponse;
import com.yang.usercenter.common.ErrorCode;
import com.yang.usercenter.common.ResultUtils;
import com.yang.usercenter.exception.BusinessException;
import com.yang.usercenter.model.domain.User;
import com.yang.usercenter.service.UserService;
import com.yang.usercenter.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yang.usercenter.constant.UserConstant.USER_LOGIN_STATE;

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



    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

//        check empty

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"params error");
        }

//        lengh
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"lenght of account must bigger than 4 ");
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"lenght of password  must bigger than 8 ");

        }

//        could not exist special signal
        String validPattern = "[a-zA-Z0-9]+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"account must combine by letter and number");
        }

//        userPassword the same as checkPassword
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"password and confirm password must be the same");
        }

//        account could not exist
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);

        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"account is exist");
        }

//        encryptPassword
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

//        insert data
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);

        boolean result = this.save(user);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"system error, please contact admin");
        }

        return user.getId();


    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {


        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"params error");

        }

//        lengh
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"lenght of account must bigger than 4 ");
        }

        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"lenght of password  must bigger than 8 ");
        }

//        could not exist special signal
        String validPattern = "[a-zA-Z0-9]+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"account must combine by letter and number");
        }


        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"user login failed, userAccount cannot match userPassword");
        }

        // user desensitization
        User safetyUser = getSafetyUser(user);

        // record user login session
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }



    @Override
    public User getSafetyUser(User user){
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setUserRole(user.getUserRole());

        return safetyUser;
    }

    @Override
    public void userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }
}




