package com.syc.china;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 授权客户端:
 * 1.通过远程调用的api,去远程的授权中心中获取AccessToken---->8769:/oauth/token...;
 * 2.对外暴露获取到的AccessToken;
 * 3.充当资源服务器(可选)
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.syc.china.mapper")
public class AuthClientServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthClientServerApplication.class, args);
    }

}
