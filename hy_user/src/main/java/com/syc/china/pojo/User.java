package com.syc.china.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Table(name = "tb_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username; //用户名，也是手机号


    private String password; //密码

    private String name;

    private String email;

    private String company; //公司名称

    private String rpersonPhone; //推荐人手机号

    private Integer memberId; //注册会员类型

    private String image; //身份证照片

    @Transient
    private String memberName; //注册会员类型名称

}
