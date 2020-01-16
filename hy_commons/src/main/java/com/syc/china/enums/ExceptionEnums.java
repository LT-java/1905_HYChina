package com.syc.china.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {

    NO_AUTHORIZED(4000,"未授权的用户"),
    REGISTER_ERROR(4001,"注册失败"),
    INVALID_VERFIY_CODE(4002,"验证码错误");
    private int code;
    private String msg;
}
