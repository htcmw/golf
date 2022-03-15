package com.reborn.golf.order.repository;

import com.reborn.golf.order.entity.OrderStatus;
import com.reborn.golf.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Orders, Long> {
    Page<Orders> getOrdersByMemberIdxAndRemovedFalse(Integer memberIdx, Pageable pageable);
    Page<Orders> getOrdersByOrderStateAndRemovedFalse(OrderStatus orderStatus, Pageable pageable);
//
//    Orders getOrdersByPartnerOrderId(String partnerOrderId);
//
//    void deleteByPartnerOrderId(String getOrdersByPartnerOrderId);
}
