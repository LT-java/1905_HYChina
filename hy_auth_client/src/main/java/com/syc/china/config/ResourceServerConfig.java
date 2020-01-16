package com.syc.china.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * EnableResourceServer:开启资源服务器功能.
 * 该程序中有个登录接口,该接口用来对外暴露用户信息以及AccessToken信息(token是由授权服务生成的).
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/js/**","/**.html","/css/**","/img/**","/layui/**","/cars/query","/user/phoneCode","/user/login","/user/register","/user/showLogin","/user/showRegister")
                .permitAll()
                .antMatchers("/**")
                .authenticated()
                .and()
                .csrf()
                .disable();
    }

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //注意:resourceId要与授权服务中clientId一致!
        resources.resourceId("auth-client")
                .tokenStore(tokenStore);
    }

}
