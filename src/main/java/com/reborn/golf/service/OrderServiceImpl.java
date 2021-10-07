package com.reborn.golf.service;

import com.reborn.golf.dto.*;
import com.reborn.golf.entity.*;
import com.reborn.golf.entity.Enum.DeliveryStatus;
import com.reborn.golf.entity.Enum.OrderStatus;
import com.reborn.golf.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;

    @Override
    @Transactional
    public PageResultDto<Object[], OrderDto> getList(PageRequestDto pageRequestDto) {
        return null;
    }

    @Override
    public PageResultDto<Object[], OrderDto> getListWithUser(Integer memberIdx, PageRequestDto pageRequestDto) {
        return null;
    }

    @Override
    @Transactional
    public Long order(Integer memberIdx, OrderDto orderDto) {
        Member member = Member.builder().idx(memberIdx).build();
        Delivery delivery = Delivery.builder()
                .address(orderDto.getAddress())
                .deliveryStatus(DeliveryStatus.READY)
                .build();

        List<OrderProduct> orderProducts = orderProductService.makeOrderProduct(orderDto.getOrderProductList());
        int totalPrice = orderProducts.stream().mapToInt(OrderProduct::getPrice).sum();
        Orders orders = Orders.builder()
                .orderState(OrderStatus.ORDER)
                .member(member)
                .delivery(delivery)
                .OrderProduct(orderProducts)
                .totalPrice(totalPrice)
                .build();
        return orders.getIdx();
    }

    @Override
    @Transactional
    public Long cancel(Integer memberIdx, Long orderIdx) {
        Optional<Orders> optionalOrders = orderRepository.findById(orderIdx);
        if(optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            orders.changeIsRemoved(false);

            orderProductService.removeOrderProduct(orders.getOrderProduct());
        }
        return null;
    }
}
