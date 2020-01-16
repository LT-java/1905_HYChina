package com.syc.china.exception;

import com.syc.china.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HChinaException  extends RuntimeException{
    private ExceptionEnums exceptionEnums;

}
