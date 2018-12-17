package com.springcloud.sellerbuyer.order.server.converter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.springcloud.sellerbuyer.order.server.dataobject.OrderDetail;
import com.springcloud.sellerbuyer.order.server.DTO.OrderDTO;
import com.springcloud.sellerbuyer.order.server.enums.ResultEnum;
import com.springcloud.sellerbuyer.order.server.exception.OrderException;
import com.springcloud.sellerbuyer.order.server.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sell
 * @description: 用户提交表单OrderForm转换器
 */
@Slf4j
public class OrderForm2OrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm) {
        Gson gson = new Gson();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>(){}.getType());
        } catch (Exception e) {
            log.error("[对象转换] 错误，string={}",orderForm.getItems());
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }
        gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>(){}.getType());
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
