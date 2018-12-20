package com.springcloud.sellerbuyer.order.server.service.impl;

import com.springcloud.sellerbuyer.order.server.enums.OrderStatusEnum;
import com.springcloud.sellerbuyer.order.server.enums.PayStatusEnum;
import com.springcloud.sellerbuyer.order.server.dataobject.OrderDetail;
import com.springcloud.sellerbuyer.order.server.dataobject.OrderMaster;
import com.springcloud.sellerbuyer.order.server.DTO.OrderDTO;
import com.springcloud.sellerbuyer.order.server.enums.ResultEnum;
import com.springcloud.sellerbuyer.order.server.exception.OrderException;
import com.springcloud.sellerbuyer.order.server.repository.OrderDetailRepository;
import com.springcloud.sellerbuyer.order.server.repository.OrderMasterRepository;
import com.springcloud.sellerbuyer.order.server.service.OrderService;
import com.springcloud.sellerbuyer.order.server.Utils.KeyUtil;
import com.springcloud.sellerbuyer.product.client.ProductClient;
import com.springcloud.sellerbuyer.product.common.DecreaseStockInput;
import com.springcloud.sellerbuyer.product.common.ProductInfoOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();

        // 1. 查询商品(数量，价格)，先得到商品Id列表，然后查询得到商品信息列表
        List<String> productIdList = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        List<ProductInfoOutput> productInfoList = productClient.listForOrder(productIdList);

        /**
         * 改造成秒杀场景 :
         *  1.把查询到的商品信息存储到Redis中
         *  2.下单的时候，要读Redis(需要Redis分布式锁)
         *  3.判断如果库存够的话，要减库存，并将新值设置进Redis(需要Redis分布式锁)
         *  4.订单详情入库和总订单入库方面: 如果数据库订单入库异常
         *      数据库回滚很方便。
         *      Redis 没有事务回滚的方法，所以需要考虑手动回滚Redis
         *  5.订单服务创建订单写入数据库，并发送消息。
         */

        // 2. 计算某一件商品的总价然后加上原来的订单总价
        BigDecimal orderAmount = new BigDecimal(0);
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            for (ProductInfoOutput productInfo : productInfoList) {
                if (productInfo.getProductId().equals(orderDetail.getProductId())) {
                    orderAmount = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmount);
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    // 订单详情入库
                    orderDetailRepository.save(orderDetail);
                }
            }
        }

        // 3. 扣库存
        List<DecreaseStockInput> decreaseStockInputList = orderDTO.getOrderDetailList().stream()
                .map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputList);


        // 4. 写入订单数据库(orderMaster)
        // API 只包含买家姓名、买家电话、买家地址、Openid、商品列表(内部包括商品id和数量)
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(String orderId) {

        // 1.先查询订单
        Optional<OrderMaster> orderMasterOptional = orderMasterRepository.findById(orderId);
        if (!orderMasterOptional.isPresent()) {
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 2.判断订单状态
        OrderMaster orderMaster = orderMasterOptional.get();
        if (OrderStatusEnum.NEW.getCode() != orderMaster.getOrderStatus()) {
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 3.修改订单状态为完结
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMasterRepository.save(orderMaster);

        // 4.由于要返回OrderDTO对象，因此还需要查询订单详情并放到OrderDTO中
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new OrderException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
