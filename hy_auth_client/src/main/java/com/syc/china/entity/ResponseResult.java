package com.syc.china.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应结果
 */
@Data
public class ResponseResult<T> implements Serializable {

    private int code = 0;

    private String error = "";

    private T data;

    public static <T> ResponseResult<T> onSuccess(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.data = data;
        return result;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }

}
