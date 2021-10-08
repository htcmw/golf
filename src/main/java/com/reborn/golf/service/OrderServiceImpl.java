package com.reborn.golf.service;

import com.reborn.golf.dto.*;
import com.reborn.golf.entity.*;
import com.reborn.golf.entity.Enum.DeliveryStatus;
import com.reborn.golf.entity.Enum.OrderStatus;
import com.reborn.golf.repository.CartRepository;
import com.reborn.golf.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;

    @Override
    @Transactional
    public PageResultDto<Orders, OrdersDto> getList(PageRequestDto pageRequestDto) {
        Page<Orders> result = orderRepository.getOrdersByRemovedFalse(pageRequestDto.getPageable(Sort.by("idx").descending()));
        Function<Orders, OrdersDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    public PageResultDto<Orders, OrdersDto> getListWithUser(Integer memberIdx, PageRequestDto pageRequestDto) {
        Page<Orders> result = orderRepository.getOrdersByMemberIdxAndRemovedFalse(memberIdx, pageRequestDto.getPageable(Sort.by("idx").descending()));
        Function<Orders, OrdersDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    @Transactional
    public Long order(Integer memberIdx, OrdersDto ordersDto) {
        Member member = Member.builder().idx(memberIdx).build();
        Delivery delivery = Delivery.builder()
                .address(ordersDto.getAddress())
                .deliveryStatus(DeliveryStatus.READY)
                .build();

        List<OrderProduct> orderProducts = orderProductService.makeOrderProduct(ordersDto.getOrderProductList());
        int totalPrice = orderProducts.stream().mapToInt(OrderProduct::getPrice).sum();
        Orders orders = Orders.builder()
                .orderState(OrderStatus.ORDER)
                .member(member)
                .delivery(delivery)
                .orderProducts(orderProducts)
                .totalPrice(totalPrice)
                .build();
        return orders.getIdx();
    }
//
//    @Override
//    @Transactional
//    public Long orderFromCart(Integer memberIdx, CartListDto cartListDto) {
//        List<Cart> carts = cartRepository.getCartsByMemberIdx(memberIdx);
//
//        List<CartDto> cartDtoList = cartListDto.getCartDtos();
//
//        List<CartDto> cartDtos = new ArrayList<>();
//        int totalPriceSum = 0;
//        for(int i = 0 ; i < carts.size(); i++){
//            CartDto cartDto = carts.get(i).toCartDto();
//            if(!cartDtoList.get(i).equals(cartDto)){
//                return null;
//            }
//            totalPriceSum += cartDto.getTotalPrice();
//            cartDtos.add(cartDto);
//        }
//        if(cartListDto.getTotalPrice() == totalPriceSum){
//
//        }
//
//        return null;
//    }

    @Override
    @Transactional
    public Long cancel(Integer memberIdx, Long orderIdx) {
        Optional<Orders> optionalOrders = orderRepository.findById(orderIdx);
        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            orders.changeIsRemoved(false);

            orderProductService.removeOrderProduct(orders.getOrderProducts());
        }
        return null;
    }
}
