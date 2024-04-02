package com.yang.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.usercenter.common.BaseResponse;
import com.yang.usercenter.common.ErrorCode;
import com.yang.usercenter.common.ResultUtils;
import com.yang.usercenter.exception.BusinessException;
import com.yang.usercenter.model.domain.User;
import com.yang.usercenter.model.domain.request.UserDelete;
import com.yang.usercenter.model.domain.request.UserLoginRquest;
import com.yang.usercenter.model.domain.request.UserRegisterRquest;
import com.yang.usercenter.model.domain.request.UserSearch;
import com.yang.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yang.usercenter.constant.UserConstant.DEFAULT_ROLE;
import static com.yang.usercenter.constant.UserConstant.USER_LOGIN_STATE;

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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRquest userRegisterRquest){

        String userAccount = userRegisterRquest.getUserAccount();
        String userPassword = userRegisterRquest.getUserPassword();
        String checkPassword = userRegisterRquest.getCheckPassword();

        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"params error");
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword);

        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRquest userLoginRquest, HttpServletRequest httpServletRequest){

        String userAccount = userLoginRquest.getUserAccount();
        String userPassword = userLoginRquest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"params error");
        }

        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);

        return ResultUtils.success(user);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;

        User user = userService.getById(currentUser.getId());

        return ResultUtils.success( userService.getSafetyUser(user));
    }

    @PostMapping("/logout")
    public BaseResponse userLogout(HttpServletRequest request){

        userService.userLogout(request);
        return ResultUtils.success(null);
    }
    @PostMapping("/search")
    public BaseResponse<List<User>> searchUser(@RequestBody UserSearch userSearch, HttpServletRequest request){
        if(!isAdmin(request)){
            return ResultUtils.success(new ArrayList<>());
        }

        String userName = userSearch.getUserName();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(userName)){
            queryWrapper.like("userName", userName);

        }

        List<User> userList = userService.list(queryWrapper);
        return ResultUtils.success(userList.stream().map((user)->{
            user.setUserPassword(null);
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList()));

    }

    @PostMapping("/delete")
    public BaseResponse deleteUser(@RequestBody UserDelete userDelete, HttpServletRequest request){

        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"you don't have this auth");
        }

        long id = userDelete.getId();

        if (id <=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"params error");
        }
        /**
         * open mybatisPlus logical delete, so it will update status not delete data
         */
        userService.removeById(id);
        return ResultUtils.success(null);
    }

    private boolean isAdmin(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute(USER_LOGIN_STATE);

        User user = (User) userObj;

        if (user == null || user.getUserRole() == DEFAULT_ROLE){
            return false;
        }
        return true;
    }



}
