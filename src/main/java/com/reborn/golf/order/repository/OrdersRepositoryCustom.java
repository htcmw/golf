package com.reborn.golf.order.repository;

import com.reborn.golf.member.entity.Member;
import com.reborn.golf.order.entity.Orders;

import java.util.Optional;

public interface OrdersRepositoryCustom {
    Optional<Orders> searchOrders(Long orderIdx, Integer memberIdx);
    Optional<Member> searchMemberWithWallet(Integer memberIdx, String email, String name);

    }
