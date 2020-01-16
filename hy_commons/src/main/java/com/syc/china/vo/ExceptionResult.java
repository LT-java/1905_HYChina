package com.syc.china.vo;

import com.syc.china.enums.ExceptionEnums;
import lombok.Data;

@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnums exceptionEnums){
        this.status = exceptionEnums.getCode();
        this.message = exceptionEnums.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}