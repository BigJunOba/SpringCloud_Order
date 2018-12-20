package com.springcloud.sellerbuyer.order.server.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @program: order
 * @description: Hystrix组件
 * @author: JunOba
 * @create: 2018-12-20 11:15
 */
@RestController
public class HystrixController {

    @HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/getProductInfoList")
    public String getProductInfoList() {
        // 1. 调用其他服务时，服务降级
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://127.0.0.1:8082/product/listForOrder",
                Arrays.asList("157875196366160022"),
                String.class);
        // 2. 自己服务发生异常时，也会触发降级
//        throw new RuntimeException("发生异常了");
    }

    private String fallback() {
        return "fallback提示:太拥挤了，请稍后再试！";
    }
}