package com.syc.china.service.impl;

import com.syc.china.dao.UserDao;
import com.syc.china.dto.LoginDTO;
import com.syc.china.entity.CommonException;
import com.syc.china.entity.ErrorCode;
import com.syc.china.entity.JWT;
import com.syc.china.entity.User;
import com.syc.china.feign.AuthClientFeign;
import com.syc.china.mapper.UserMapper;
import com.syc.china.service.UserService;
import com.syc.china.util.BPwdEncoderUtils;
import com.syc.china.utils.NumberUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthClientFeign clientFeign;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "user:code:phone:";


    public void sendSms(String phone) {
        //生成验证码
        String code = NumberUtils.generateCode(6);
        try{
            Map<String,String> msg = new HashMap<>();
            msg.put("code",code);
            msg.put("phone",phone);
            amqpTemplate.convertAndSend("wlkg.sms.exchange","sms.verify.code",msg);


            //将code存入redis
            redisTemplate.opsForValue().set(KEY_PREFIX + phone,code,5, TimeUnit.MINUTES);
        }catch (Exception e){
            e.printStackTrace();
        }
    }










    @Override
    public LoginDTO login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        if (!BPwdEncoderUtils.matches(password, user.getPassword())) {
            throw new CommonException(ErrorCode.USER_PASSWORD_ERROR);
        }

        //远程调用:获取AccessToken
        //clientId:secret  ---> auth-client:123456
        //把auth-client:123456进行Base64编码,得到的结果=YXV0aC1jbGllbnQ6MTIzNDU2
        JWT jwt = clientFeign.getAccessToken("Basic YXV0aC1jbGllbnQ6MTIzNDU2", "password", username, password);
        if (jwt == null) {
            throw new CommonException(ErrorCode.GET_TOKEN_FAIL);
        }

        LoginDTO dto = new LoginDTO();
        dto.setUser(user);
        dto.setAccessToken(jwt.getAccess_token());
        return dto;
    }

    @Override
    public User findUserByName(String username) {

        return userDao.findByUsername(username);
    }

    @Override
    public User register(User user) {

        return userDao.save(user);
    }

    public String queryRoleName(Integer id) {
        return userMapper.queryRoleName(id);
    }
}
