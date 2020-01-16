package com.syc.china.service;

import com.syc.china.mapper.UserMapper;
import com.syc.china.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public User login(User user) {
        return userMapper.selectOne(user);
    }
}
