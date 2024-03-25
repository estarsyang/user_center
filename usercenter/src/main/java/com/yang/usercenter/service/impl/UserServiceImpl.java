package com.yang.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.usercenter.model.domain.User;
import com.yang.usercenter.service.UserService;
import com.yang.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author ddayang
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-03-25 23:53:33
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




