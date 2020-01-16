package com.syc.china.service;

import com.syc.china.dto.LoginDTO;
import com.syc.china.entity.User;
import com.syc.china.utils.NumberUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public interface UserService {

    LoginDTO login(String username, String password);

    User findUserByName(String username);

    User register(User user);



}
