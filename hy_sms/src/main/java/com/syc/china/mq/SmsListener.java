package com.syc.china.mq;

import com.aliyuncs.CommonResponse;

import com.syc.china.config.SmsProperties;
import com.syc.china.utils.SmsUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private SmsProperties prop;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    value = "sms.verify.code.queue",
                    durable = "true"
            ),
            exchange = @Exchange(
                    value = "wlkg.sms.exchange",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}
    ))
    public void  listenSms(Map<String,String> msg){


        if(CollectionUtils.isEmpty(msg)){
            //放弃处理
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");

        if(StringUtils.isEmpty(phone)|| StringUtils.isEmpty(code)){
            //放弃处理
            return;
        }
        //发送消息
        CommonResponse response = smsUtils.sendSms(phone,code);
    }


}
