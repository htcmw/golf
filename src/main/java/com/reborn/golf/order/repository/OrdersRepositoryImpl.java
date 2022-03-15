package com.reborn.golf.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reborn.golf.member.entity.Member;
import com.reborn.golf.order.entity.Orders;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.reborn.golf.member.entity.QMember.member;
import static com.reborn.golf.member.entity.QWallet.wallet;
import static com.reborn.golf.order.entity.QOrderProduct.orderProduct;
import static com.reborn.golf.order.entity.QOrders.orders;

public class OrdersRepositoryImpl implements OrdersRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public OrdersRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Orders> searchOrders(Long orderIdx, Integer memberIdx) {
        return Optional.ofNullable(queryFactory
                .select(orders)
                .from(orders).leftJoin(orders.orderProducts, orderProduct).fetchJoin()
                .where(orders.idx.eq(orderIdx), orders.member.idx.eq(memberIdx))
                .fetchOne());
    }

    @Override
    public Optional<Member> searchMemberWithWallet(Integer memberIdx, String email, String name) {
        return Optional.ofNullable(queryFactory
                .select(member)
                .from(member).join(member.wallet, wallet).fetchJoin()
                .where(member.idx.eq(memberIdx), member.email.eq(email), member.name.eq(name))
                .fetchOne());
    }

}
