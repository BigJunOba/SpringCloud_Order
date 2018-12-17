package com.springcloud.sellerbuyer.order.controller;

import com.springcloud.sellerbuyer.order.DTO.CartDTO;
import com.springcloud.sellerbuyer.order.client.ProductClient;
import com.springcloud.sellerbuyer.order.dataobject.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @program: order
 * @description: 订单服务作为http client端
 * @author: JunOba
 * @create: 2018-12-16 21:10
 */
@RestController
@Slf4j
public class ClientController {

    @Autowired
    private ProductClient productClient;

    @GetMapping("/getProductMsg")
    public String getProductMsg() {

        String response = productClient.productMsg();
        log.info("response = {}", response);
        return response;
    }

    @GetMapping("/getProductList")
    public String getProductList() {
        List<ProductInfo> productInfoList = productClient.listForOrder(Arrays.asList("164103465734242707"));
        log.info("response = {}", productInfoList);
        return "ok";
    }

    @GetMapping("/decreaseStock")
    public String productDecreaseStock() {
        productClient.decreaseStock(Arrays.asList(new CartDTO("164103465734242707", 3)));
        return "ok";
    }

}
