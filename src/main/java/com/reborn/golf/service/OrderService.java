package com.reborn.golf.service;

import com.reborn.golf.dto.shop.OrdersDto;
import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.entity.Orders;
import com.siot.IamportRestClient.exception.IamportResponseException;

import java.io.IOException;

public interface OrderService {

    PageResultDto<Orders, OrdersDto> getListWithUser(Integer memberIdx, PageRequestDto pageRequestDto);

    PageResultDto<Orders, OrdersDto> getList(PageRequestDto pageRequestDto);

    String order(Integer memberIdx, OrdersDto ordersDto) throws IamportResponseException, IOException;

//    Long orderFromCart(Integer memberIdx, CartListDto cartListDto);

    Long cancel(Integer memberIdx, Long orderIdx);

    default OrdersDto entityToDto(Orders orders){
        return OrdersDto.builder()
                .idx(orders.getIdx())
                .totalPrice(orders.getTotalPrice())
                .userName(orders.getMember().getName())
                .userEmail(orders.getMember().getEmail())
                .userAddress(orders.getDelivery().getAddress())
                .orderState(orders.getOrderState().name())
                .deliveryStatus(orders.getDelivery().getDeliveryStatus().name())
                .orderProductList(orders.toOrderProductDto())
                .impUid(orders.getImpUid())
                .orderName(orders.getOrderName())
                .orderNumber(orders.getOrderNumber())
                .tokenAmount(orders.getTokenAmount())
                .regDate(orders.getRegDate())
                .modDate(orders.getModDate())
                .build();
    }

}
