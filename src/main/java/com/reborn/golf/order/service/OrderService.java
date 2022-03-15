package com.reborn.golf.order.service;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.order.dto.OrderProductDto;
import com.reborn.golf.order.dto.OrdersDto;
import com.reborn.golf.order.entity.OrderProduct;
import com.reborn.golf.order.entity.Orders;
import com.reborn.golf.product.dto.ProductImageDto;
import com.reborn.golf.product.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface OrderService {

    PageResultDto<Orders, OrdersDto> getListWithUser(Integer memberIdx, PageRequestDto pageRequestDto);

    PageResultDto<Orders, OrdersDto> getListWithState(String state, PageRequestDto pageRequestDto);

    OrdersDto order(Integer memberIdx, OrdersDto ordersDto);

    Long cancelAll(Integer memberIdx, Long orderIdx);

    default OrdersDto entitiesToDto(Orders orders, List<OrderProduct> orderProducts){
        List<OrderProductDto> orderProductDtos = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {

            Product product = orderProduct.getProduct();

            List<ProductImageDto> productImageDtoList = product.getProductImages().stream().map(productImage ->
                    ProductImageDto.builder()
                            .imgName(productImage.getImgName())
                            .path(productImage.getPath())
                            .uuid(productImage.getUuid())
                            .build()
            ).collect(Collectors.toList());

            OrderProductDto orderProductDto = OrderProductDto.builder()
                    .orderProductIdx(orderProduct.getIdx())
                    .price(orderProduct.getPrice())
                    .quantity(orderProduct.getQuantity())
                    .productIdx(product.getIdx())
                    .title(product.getTitle())
                    .brand(product.getBrand())
                    .content(product.getContent())
                    .imageDtoList(productImageDtoList)
                    .isRemoved(orderProduct.isRemoved())
                    .build();
            orderProductDtos.add(orderProductDto);
        }

        return OrdersDto.builder()
                .idx(orders.getIdx())
                .impUid(orders.getImpUid())
                .orderNumber(orders.getOrderNumber())
                .orderName(orders.getOrderName())
                .totalPrice(orders.getTotalPrice())
                .tokenPrice(orders.getTokenPrice())
                .tokenAmount(orders.getTokenAmount())
                .orderState(orders.getOrderState().name())
                .userEmail(orders.getMember().getEmail())
                .userName(orders.getMember().getName())
                .orderProductsCount(orders.getOrderProductsCount())
                .pointAmountToBuyer(orders.getPointAmountToBuyer())
                .orderProductList(orderProductDtos)
                .recipient(orders.getDelivery().getRecipient())
                .recipientAddress(orders.getDelivery().getRecipientAddress())
                .recipientPhone(orders.getDelivery().getRecipientPhone())
                .deliveryStatus(orders.getDelivery().getDeliveryStatus().name())
                .deliveryMessage(orders.getDelivery().getDeliveryMessage())
                .regDate(orders.getRegDate())
                .modDate(orders.getModDate())
                .build();
    }
    default OrdersDto entityToDto(Orders orders){
        return OrdersDto.builder()
                .idx(orders.getIdx())
                .impUid(orders.getImpUid())
                .orderNumber(orders.getOrderNumber())
                .orderName(orders.getOrderName())
                .totalPrice(orders.getTotalPrice())
                .tokenPrice(orders.getTokenPrice())
                .tokenAmount(orders.getTokenAmount())
                .orderState(orders.getOrderState().name())
                .userEmail(orders.getMember().getEmail())
                .userName(orders.getMember().getName())
                .orderProductsCount(orders.getOrderProductsCount())
                .orderProductList(orders.toOrderProductDto())
                .recipient(orders.getDelivery().getRecipient())
                .recipientAddress(orders.getDelivery().getRecipientAddress())
                .recipientPhone(orders.getDelivery().getRecipientPhone())
                .deliveryStatus(orders.getDelivery().getDeliveryStatus().name())
                .deliveryMessage(orders.getDelivery().getDeliveryMessage())
                .regDate(orders.getRegDate())
                .modDate(orders.getModDate())
                .build();
    }

}
