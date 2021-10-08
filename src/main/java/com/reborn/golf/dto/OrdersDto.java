package com.reborn.golf.dto;

import com.reborn.golf.entity.Delivery;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.OrderProduct;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrdersDto {

    private Long idx;

    private Integer totalPrice;

    private String email;

    private String name;

    private String address;

    private String orderState;

    private String deliveryStatus;

    private List<OrderProductDto> orderProductList;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
