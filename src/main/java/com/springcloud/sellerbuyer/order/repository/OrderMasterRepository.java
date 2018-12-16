package com.springcloud.sellerbuyer.order.repository;

import com.springcloud.sellerbuyer.order.dataobject.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {
}
