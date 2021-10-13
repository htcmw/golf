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
    //아임포트 아이디
    private String impUid;
    //아임포트 주문 결제 idx
    private String orderNumber;
    //주문 이름
    private String orderName;
    //주문한 총 금액
    private Integer totalPrice;
    //포인트
    private Integer tokenAmount;
    //상품 종류 수
    private Integer orderProductsCount;
    //유저 이메일
    private String userEmail;
    //유저 이름
    private String userName;
    //유저 주소
    private String userAddress;
    //유저 전화번호
    private String userPhone;
    //주문 상태
    private String orderState;
    //배달 상태
    private String deliveryStatus;
    //구매 물품 및 수량
    private List<OrderProductDto> orderProductList;
    //주문 날짜 및 시간
    private LocalDateTime regDate;
    //주문 수정 날짜 및 시간
    private LocalDateTime modDate;
}
