package com.springcloud.sellerbuyer.order.server;

import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: order
 * @description: Mq消息发送方
 * @author: JunOba
 * @create: 2018-12-19 11:02
 */
@Component
public class MqSenderTest extends OrderApplicationTests {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void send() {
        amqpTemplate.convertAndSend("myQueue", "now " + new Date());
    }

    @Test
    public void sendComputerOrder() {
        amqpTemplate.convertAndSend("myOrder", "computer", "now " + new Date());
    }

    @Test
    public void sendFruitOrder() {
        amqpTemplate.convertAndSend("myOrder", "fruit", "now " + new Date());
    }
}
