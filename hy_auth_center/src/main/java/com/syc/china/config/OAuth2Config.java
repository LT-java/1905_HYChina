package com.syc.china.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * EnableAuthorizationServer:开启授权服务器功能
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Value("${auth.client-id}")
    private String clientId;

    @Value("${auth.secret}")
    private String secret;

    /**
     * 对连接该授权服务器的所有客户端进行一些设置.
     * OAuth2中有4种授权码模式:
     * 1.授权码授权;
     * 2.客户端凭证授权;
     * 3.资源拥有者的密码授权;
     * 4.隐式授权.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //client-id:客户端程序的id,也就是客户端名称.
                //注意:这样的配置信息可以放在配置文件中,引用即可.
                //auth-client
                .withClient(clientId)
                //123456
                .secret(secret)
                .scopes("service")
                .autoApprove(true)
                //授权模式
                .authorizedGrantTypes("authorization_code", "refresh_token", "password", "implicit")
                //设置令牌的过期时间
                .accessTokenValiditySeconds(24 * 3600);
    }

    /**
     * 负责AccessToken与Jwt之间的转换,具体的转换过程利用RSA算法进行加密转换.
     * 在本案例中需要管理私钥文件xxx.jks
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //关联私钥文件
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(new ClassPathResource("syc-jwt.jks"), "syc123".toCharArray());
        //本案例中,私钥文件的别名与私钥文件的名称一致
        converter.setKeyPair(keyFactory.getKeyPair("syc-jwt"));
        return converter;
    }

    @Bean
    public JwtTokenStore jwtTokenStore() {
        //new RedisTokenStore(redisConnectionFactory);
        //1.创建AccessToken--->OAuth2内部已经提供了接口,负责生成AccessToken;
        //2.再利用Jwt协议把AccessToken进行加密.
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    /**
     * 对EndPoint进行设置
     * 1. 授权Endpoint;
     * 2. token endpoint;
     * 3. 重定向的endpoint.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //设置令牌的存储位置,令牌的具体生成策略
        endpoints.tokenStore(jwtTokenStore())
                .tokenEnhancer(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager);
    }

    /**
     * 对授权服务器的安全性做配置.
     * 检查令牌的接口,直接放行;
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //对所有的客户端来说,允许所有客户端以表单认证的方式来进行认证
        security.allowFormAuthenticationForClients()
                //检查令牌的接口,直接放行;
                .checkTokenAccess("permitAll()")
                //在新版的springSecurity中,如果不配置该方法,会产生异常.
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}
