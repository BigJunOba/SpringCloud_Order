package com.springcloud.sellerbuyer.order.service.impl;

import com.springcloud.sellerbuyer.order.dataobject.OrderMaster;
import com.springcloud.sellerbuyer.order.dto.OrderDTO;
import com.springcloud.sellerbuyer.order.enums.OrderStatusEnum;
import com.springcloud.sellerbuyer.order.enums.PayStatusEnum;
import com.springcloud.sellerbuyer.order.repository.OrderDetailRepository;
import com.springcloud.sellerbuyer.order.repository.OrderMasterRepository;
import com.springcloud.sellerbuyer.order.service.OrderService;
import com.springcloud.sellerbuyer.order.Utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
////        List<CartDTO> cartDTOList = new ArrayList<>();
//
//        // 1. 查询商品(数量，价格)
//        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
//            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
//            if (productInfo == null) {
//                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
////                throw new ResponseBankException();
//            }
//            // 2. 计算某一件商品的总价然后加上原来的订单总价
//            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()))
//                    .add(orderAmount);
//            // 3. 订单详情入库，对应 POST /sell/buyer/order/create 这个API
//            // 只包含商品id和数量)
//            orderDetail.setDetailId(KeyUtil.genUniqueKey());
//            orderDetail.setOrderId(orderId);
//            BeanUtils.copyProperties(productInfo, orderDetail);
//            orderDetailRepository.save(orderDetail);
//
////            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
////            cartDTOList.add(cartDTO);
//        }

        // 3. 写入订单数据库(orderMaster)
        // API 只包含买家姓名、买家电话、买家地址、Openid、商品列表(内部包括商品id和数量)
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

//        // 4. 下单成功，扣库存
//        // 通过Lambda表达式简洁写法可以不污染上面的代码
//        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
//                new CartDTO(e.getProductId(), e.getProductQuantity())
//        ).collect(Collectors.toList());
//        productService.decreaseStock(cartDTOList);
//
//        // 发送websocket消息
//        webSocket.sendMessage("您有新的订单，请注意查收！订单号 : " + orderDTO.getOrderId());
        return orderDTO;
    }

}
