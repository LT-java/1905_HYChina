package com.syc.china.feign.impl;

import com.syc.china.entity.JWT;
import com.syc.china.feign.AuthClientFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthClientFeignHystrix implements AuthClientFeign {

    @Override
    public JWT getAccessToken(String authorization, String grantType, String username, String password) {
        log.warn("authorization="+authorization+",grantType="+grantType+",username="+username+",password="+password);
        return null;
    }

}
