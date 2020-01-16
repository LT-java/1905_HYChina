package com.sys.china.rabbit;


import com.alibaba.fastjson.JSON;
import com.syc.china.pojo.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReceiveMsg {


    //监听消息
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    value = "china.log.code.queue",
                    durable = "true"
            ),
            exchange = @Exchange(
                    value = "china.log.exchange",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"),
            key = {"china.log.code"}
    ))
    public void receiveMsg(String json){
        SysLog sysLog = JSON.parseObject(json,SysLog.class);

        //把该消息自动传输到elk环境
        log.warn("msg=" + sysLog);
    }

}
