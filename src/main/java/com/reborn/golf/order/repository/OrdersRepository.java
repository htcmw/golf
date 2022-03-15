package com.reborn.golf.order.repository;

import com.reborn.golf.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersRepositoryCustom {
    Page<Orders> getOrdersByMemberIdxAndRemovedFalse(Integer memberIdx, Pageable pageable);
}
