package com.syc.china.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class SysLog implements Serializable {

    private String methodName;

    private String params;

    private String operation;

    private Date date;

}
