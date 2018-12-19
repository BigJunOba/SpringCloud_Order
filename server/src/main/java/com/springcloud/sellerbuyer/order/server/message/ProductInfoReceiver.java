package com.springcloud.sellerbuyer.order.server.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.springcloud.sellerbuyer.order.server.Utils.JsonUtil;
import com.springcloud.sellerbuyer.product.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: order
 * @description: 订单方面接收商品方发送的消息
 * @author: JunOba
 * @create: 2018-12-19 16:12
 */
@Component
@Slf4j
public class ProductInfoReceiver {

    private static final String PRODUCT_STOCK_TEMPLATE = "product_stock_%s";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void process(String message) {

        // message ==> product-common 里的 ProductInfoOutput 对象
        List<ProductInfoOutput> productInfoOutputList = (List<ProductInfoOutput>)JsonUtil.
                fromJson(message, new TypeReference<List<ProductInfoOutput>>() {});
        log.info("[从【{}】接收到消息] {}", "productInfo", productInfoOutputList);

        // 存储到Redis中
        for (ProductInfoOutput productInfoOutput : productInfoOutputList) {
            stringRedisTemplate.opsForValue().set(String.format(PRODUCT_STOCK_TEMPLATE, productInfoOutput.getProductId()),
                    String.valueOf(productInfoOutput.getProductStock()));
        }
    }
}
