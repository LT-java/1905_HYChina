package com.syc.china.advice;

import com.syc.china.enums.ExceptionEnums;
import com.syc.china.exception.HChinaException;
import com.syc.china.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(HChinaException.class)
    public ResponseEntity<ExceptionResult> handleException(HChinaException e){
        ExceptionEnums em = e.getExceptionEnums();
        //我们暂定返回状态码为400， 然后从异常中获取友好提示信息
        return ResponseEntity.status(em.getCode()).body(new ExceptionResult(em));
    }
}
