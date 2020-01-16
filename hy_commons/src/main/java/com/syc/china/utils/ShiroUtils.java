package com.syc.china.utils;



import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

/**
 * Shiro工具类
 */
public class ShiroUtils {

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    //存储信息到session中
    public static void setAttribute(String key, String v) {
        getSession().setAttribute(key, v);
    }

    //从session中获取信息
    public static Object getAttribute(String key) {
        return getSession().getAttribute(key);
    }

    //存储验证码到session中
    public static void setKaptcha(String code) {
        setAttribute(SysConstant.CAPTCHA_KEY, code);
    }

    //存储验证码到session中
    public static String getKaptcha() {
        return getAttribute(SysConstant.CAPTCHA_KEY) + "";
    }

    //获取登录用户
    public static User getUserEntity() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

/*    //获取用户id
    public static long getUserId() {
        return getUserEntity().getUserId();
    }*/

    //退出登录
    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

}
