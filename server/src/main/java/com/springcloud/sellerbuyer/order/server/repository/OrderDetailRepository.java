package com.springcloud.sellerbuyer.order.server.repository;

import com.springcloud.sellerbuyer.order.server.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
