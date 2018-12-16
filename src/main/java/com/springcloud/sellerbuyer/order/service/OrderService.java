package com.springcloud.sellerbuyer.order.service;

import com.springcloud.sellerbuyer.order.dto.OrderDTO;


public interface OrderService {

    /** 创建订单 */
    OrderDTO create(OrderDTO orderDTO);

}
