package com.springcloud.sellerbuyer.order.server.service;

import com.springcloud.sellerbuyer.order.server.DTO.OrderDTO;


public interface OrderService {

    /** 创建订单 */
    OrderDTO create(OrderDTO orderDTO);

}
