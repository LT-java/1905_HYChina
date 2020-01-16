package com.syc.china.service;

import com.alibaba.fastjson.JSON;
import com.syc.china.pojo.SysLog;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    @Autowired
    private AmqpTemplate template;

    public void transferLog(SysLog sysLog) {
        template.convertAndSend("china.log.exchange","china.log.code",JSON.toJSONString(sysLog));
    }

}
