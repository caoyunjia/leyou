package com.leyou.sms.mq;

import com.leyou.common.utils.JsonUtils;
import com.leyou.sms.utils.SmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
@Slf4j
@Component
public class SmsListener {


    @Autowired
    private SmsUtils smsUtils;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sms.verify.code.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ly.sms.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"sms.verify.code"}))
    public void listenSmsMessage(Map<String,String> msg){
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.remove("phone");
        if (StringUtils.isBlank(phone)) {
            return;
        }
        smsUtils.sendSms(phone , JsonUtils.serialize(msg));
    }
}