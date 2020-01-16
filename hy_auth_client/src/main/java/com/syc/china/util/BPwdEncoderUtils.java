package com.syc.china.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 */
public class BPwdEncoderUtils {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 用BCryptPasswordEncoder加密
     */
    public static String  bCryptPassword(String password){
        return encoder.encode(password);
    }


    /**
     *
     * 密码匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword){
        return encoder.matches(rawPassword,encodedPassword);
    }

}
