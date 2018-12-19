package com.springcloud.sellerbuyer.order.server.message;

import com.springcloud.sellerbuyer.order.server.DTO.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @program: order
 * @description: Stream接收
 * @author: JunOba
 * @create: 2018-12-19 14:56
 */
@Component
@EnableBinding(StreamClient.class)
@Slf4j
public class StreamReceiver {

//    @StreamListener(StreamClient.INPUT)
//    public void process(Object message) {
//        log.info("[StreamReceiver] : {}", message);
//    }

    /**
     * 接收OrderDTO对象
     * @param message
     */
    @StreamListener(StreamClient.INPUT)
    @SendTo(StreamClient.INPUT2)
    public String process(String message) {
        log.info("[StreamReceiver] : {}", message);
        // 在接收到之后，发送mq消息到StreamClient.INPUT2
        return "reveived.";
    }

    @StreamListener(StreamClient.INPUT2)
    public void process2(String message) {
        log.info("[StreamReceiver2] : {}", message);
    }

}
