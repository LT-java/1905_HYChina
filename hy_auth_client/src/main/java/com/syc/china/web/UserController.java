package com.syc.china.web;

import com.syc.china.dto.LoginDTO;
import com.syc.china.entity.ResponseResult;
import com.syc.china.entity.User;
import com.syc.china.enums.ExceptionEnums;
import com.syc.china.exception.HChinaException;
import com.syc.china.mapper.UserMapper;
import com.syc.china.service.UserService;
import com.syc.china.service.impl.UserServiceImpl;
import com.syc.china.util.BPwdEncoderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    static final String KEY_PREFIX = "user:code:phone:";

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @GetMapping("/showLogin")
    public String showLogin(){
        return "index";
    }


    @PostMapping("/phoneCode")
    @ResponseBody
    public ResponseEntity<Void> sendSms(@RequestParam("phone")String phone){
        userServiceImpl.sendSms(phone);
        return ResponseEntity.ok(null);
    }



    @PostMapping("/register")
    public String register(User user,@RequestParam("code")String code) {

        //获取redis中的key
        String key = KEY_PREFIX + user.getUsername();
        //获取redis中存储的验证码
        String redisCode = redisTemplate.opsForValue().get(key);

        //检查是否正确
        if(StringUtils.isEmpty(redisCode) || !redisCode.equals(code)){
            throw new HChinaException(ExceptionEnums.INVALID_VERFIY_CODE);
        }

        String pwd = BPwdEncoderUtils.bCryptPassword(user.getPassword());
        user.setPassword(pwd);
        User newUser = userService.register(user);
        if(newUser == null){
            throw new  RuntimeException();
        }

        userMapper.insertMid(user.getId(),user.getMember_id());

        redisTemplate.delete(key);

        return "redirect:/user/showLogin";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        LoginDTO dto = userService.login(username, password);
        session.setAttribute("token",dto.getAccessToken());
        return "redirect:/cars/query";
    }

    /**
     * 前后端认证授权交互过程:
     * 1.ajax调用login接口-->json解析-->得到AccessToken字符串;
     * 2.ajax获取用户信息,必须携带着上面得到的AccessToken字符串,需要利用请求头(由前端程序员去把token设置到请求头中)去携带.
     * 格式如下:
     * Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzkwNjk0MDgsInVzZXJfbmFtZSI6InN5YyIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQURNSU4iXSwianRpIjoiMmUwNjdkNDItNmJiYy00OGQ0LWE5OTgtNmExZDI4ZjYwNjdjIiwiY2xpZW50X2lkIjoiYXV0aC1jbGllbnQiLCJzY29wZSI6WyJzZXJ2aWNlIl19.mR-Mb9ZN11r9fS1BprwP5MhwJsnEwSS6zBlzKaArhj_WPpaMP8NfQsP0aEuyGn2XwAURj0HhpHGwerO6_lJtFDuTcw5YZIVyDgGWJdSiO6DClVLlRFX8_e8gVgcUGDE3s8JGA7SHSq4euDDAoocyEdhLNe5ioI57OK6Z5It8svWhmghEn0JAtxvLdLAchZqGP4Pz-Zr4kAm_HWioZzWTLDnmJ8UF5hrmrEZ7szOIUPkQWOfS_jChQb4XU7B08yKf0SdSr0RTgF2jeCTmE2zX2yntqKydF1HRPWX4cuszbK8CTsujjkugsWLM8VGP7sjkjq1RZY0WRpXKKVKe3s3szA
     * <p>
     * PreAuthorize("hasAuthority('ROLE_ADMIN')")
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    @ResponseBody
    public ResponseResult<User> showUserInfo(@PathVariable("username") String username, @RequestHeader("authorization") String authorization) {
        log.warn("authorization=" + authorization);
        User user = userService.findUserByName(username);
        return ResponseResult.onSuccess(user);
    }

}
