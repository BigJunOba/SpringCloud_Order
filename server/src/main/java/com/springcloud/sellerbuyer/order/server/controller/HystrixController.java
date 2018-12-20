package com.springcloud.sellerbuyer.order.server.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {

    // 1.指定降级方法名称
//    @HystrixCommand(fallbackMethod = "fallback")

    // 2.超时时间配置，配置成3秒，3秒之后如果没有请求到，就触发降级
//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
//    })

//    // 3.服务熔断
//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),                      // 设置熔断
//            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),         // 断路器的最小请求数
//            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),   // 休眠时间窗口10秒
//            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")        // 错误比率
//    })

    // 4.使用配置文件中的配置项进行配置
    @HystrixCommand
    @GetMapping("/getProductInfoList")
    public String getProductInfoList(@RequestParam("number") Integer numbe) {
        if (numbe % 2 == 0) {
            return "success";
        }
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

    private String defaultFallback() {
        return  "默认defaultFallback提示";
    }
}
