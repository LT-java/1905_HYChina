package com.syc.china;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 授权服务器:
 * 1.生成access-token(访问令牌)--->有一个对应的接口:/oauth/token;
 * 2.验证access-token--->也有一个对应的接口.
 */
@SpringBootApplication
@EnableEurekaClient
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
