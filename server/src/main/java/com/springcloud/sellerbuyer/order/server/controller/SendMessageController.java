package com.springcloud.sellerbuyer.order.server.controller;

import com.springcloud.sellerbuyer.order.server.DTO.OrderDTO;
import com.springcloud.sellerbuyer.order.server.dataobject.OrderDetail;
import com.springcloud.sellerbuyer.order.server.message.StreamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: order
 * @description: 通过Stream发送消息
 * @author: JunOba
 * @create: 2018-12-19 14:58
 */
@RestController
public class SendMessageController {

    @Autowired
    private StreamClient streamClient;

    @GetMapping("/sendMessage")
    public void process() {
        streamClient.output().send(MessageBuilder.withPayload("now " + new Date()).build());
    }

    /**
     * 发送对象消息
     */
    @GetMapping("/sendObjectMessage")
    public void processObject() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("123456");
        streamClient.output().send(MessageBuilder.withPayload(orderDTO).build());
    }
}
