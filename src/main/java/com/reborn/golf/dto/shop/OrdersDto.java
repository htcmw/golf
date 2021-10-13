package com.reborn.golf.dto.shop;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrdersDto {
    //주문 테이블 pk
    private Long idx;
    //주문한 총 금액
    private Integer totalPrice;
    //유저 이메일
    private String userEmail;
    //유저 이름
    private String userName;
    //유저 주소
    private String userAddress;



    private String orderState;

    private String deliveryStatus;

    private List<OrderProductDto> orderProductList;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
