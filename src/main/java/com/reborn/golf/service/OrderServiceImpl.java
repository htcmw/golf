package com.reborn.golf.service;

import com.reborn.golf.api.IamportManager;
import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.shop.OrdersDto;
import com.reborn.golf.entity.*;
import com.reborn.golf.entity.Enum.DeliveryStatus;
import com.reborn.golf.entity.Enum.OrderStatus;
import com.reborn.golf.repository.OrderRepository;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;
    private final IamportManager iamportManager;
    @Override
    public PageResultDto<Orders, OrdersDto> getListWithUser(Integer memberIdx, PageRequestDto pageRequestDto) {
        Page<Orders> result = orderRepository.getOrdersByMemberIdxAndRemovedFalse(memberIdx, pageRequestDto.getPageable(Sort.by("regDate").descending()));
        Function<Orders, OrdersDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    @Transactional
    public PageResultDto<Orders, OrdersDto> getListWithState(String state, PageRequestDto pageRequestDto) {
        Page<Orders> result = null;
        if(state.equals("ORDER")){
            result = orderRepository.getOrdersByOrderStateAndRemovedFalse(OrderStatus.ORDER,pageRequestDto.getPageable(Sort.by("regDate").descending()));
        }
        else if(state.equals("DELIVERY")){
            result = orderRepository.getOrdersByOrderStateAndRemovedFalse(OrderStatus.DELIVERY,pageRequestDto.getPageable(Sort.by("regDate").descending()));
        }
        else if(state.equals("CANCEL")){
            result = orderRepository.getOrdersByOrderStateAndRemovedFalse(OrderStatus.CANCEL,pageRequestDto.getPageable(Sort.by("regDate").descending()));
        }
        Function<Orders, OrdersDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    @Transactional
    public String order(Integer memberIdx, OrdersDto ordersDto) throws IamportResponseException, IOException {
        log.info(ordersDto);
//        iamportManager.paymentVerification(ordersDto);

        List<OrderProduct> orderProducts = orderProductService.makeOrderProduct(ordersDto.getOrderProductList());
        Member member = Member.builder().idx(memberIdx).build();
        Delivery delivery = Delivery.builder().address(ordersDto.getUserAddress()).deliveryStatus(DeliveryStatus.NULL).build();
        Integer totalPrice = orderProducts.stream().mapToInt(OrderProduct::getPrice).sum();

        Orders orders = Orders.builder()
                .orderState(OrderStatus.ORDER)
                .member(member)
                .impUid(ordersDto.getImpUid())
                .orderName(ordersDto.getOrderName())
                .orderNumber(ordersDto.getOrderNumber())
                .tokenAmount(ordersDto.getTokenAmount())
                .delivery(delivery)
                .orderProducts(orderProducts)
                .totalPrice(totalPrice)
                .orderProductsCount(orderProducts.size())
                .build();

        orderRepository.save(orders);

        return orders.getIdx().toString();

    }

    @Override
    @Transactional
    public Long cancel(Integer memberIdx, Long orderIdx) {

        Optional<Orders> optionalOrders = orderRepository.findById(orderIdx);

        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            orders.changeIsRemoved(true);
            orders.setOrderState(OrderStatus.CANCEL);
            orderProductService.removeOrderProduct(orders.getOrderProducts());
            orderRepository.save(orders);

            return orders.getIdx();
        }
        return null;
    }
}
