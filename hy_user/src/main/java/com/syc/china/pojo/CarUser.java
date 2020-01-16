package com.syc.china.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_car_user")
@Data
public class CarUser {

    @Id
    private Integer UserId;

    private String company;

    private String name;

    private String phone;

    private String idCard;//身份证

    private String Certificates; //资质营业照

    private String email;

    private String wechat;

    private String QQ;
}
