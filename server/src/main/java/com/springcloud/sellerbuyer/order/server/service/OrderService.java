package com.springcloud.sellerbuyer.order.server.service;

import com.springcloud.sellerbuyer.order.server.DTO.OrderDTO;


public interface OrderService {

    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);

    /**
     * 完结订单(只能卖家来操作)
     * @param orderId
     * @return
     */
    OrderDTO finish(String orderId);
}
