package com.leyou.sms.test;

import com.leyou.sms.utils.SmsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsTest {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SmsUtils smsUtils;
    @Test
    public void testSendSendMq(){
        HashMap<String, String> msg = new HashMap<>();
        msg.put("phone","15517555778");
        msg.put("code", "12345");
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code.queue", msg);

    }

    @Test
    public void testSendSms(){
        HashMap<String, String> msg = new HashMap<>();
        msg.put("phone","15517555778");
        msg.put("code", "12345");
        smsUtils.sendSms("15517555778","{'code':'123456'}");
    }
}
