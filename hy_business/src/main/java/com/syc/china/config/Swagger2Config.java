package com.syc.china.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * EnableSwagger2:开启swagger功能
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //指定接口文档的生成范围
                .apis(RequestHandlerSelectors.basePackage("com.syc.china.controller"))
                .build()
                .apiInfo(createApiInfo());
    }

    @Bean
    public ApiInfo createApiInfo(){
        return new ApiInfoBuilder()
                .title("危运中国在线接口文档")
                .description("危运中国在线接口文档,记录车源货源的相关接口")
                .version("1.0.1")
                .license("文档协议")
                .contact(new Contact("ZK","168941866@qq.com","1689418664@qq.com"))
                .build();
    }

}