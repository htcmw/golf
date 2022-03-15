package com.reborn.golf.order.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrdersDto {
    private Long idx;   //주문 테이블 pk
    private String impUid;  //아임포트 아이디
    private String orderNumber; //아임포트 주문 결제 idx
    private String orderName;   //주문 이름
    private Integer totalPrice; //주문한 총 금액
    private Double tokenPrice;  //포인트 금액
    private Long tokenAmount;   //포인트 수량
    private String orderState;  //주문 상태

    /* orderProducts 정보 */
    private Integer allProductPrice; //상품들의 총 금액
    private Integer orderProductsCount; //상품 종류 수, entity에 없음
    private List<OrderProductDto> orderProductList; //구매 물품 및 수량

    /* 유저 정보 */
    private String userEmail;   //결제한 사람 이메일
    private String userName;    //결제한 사람 이름

    /* 배달 정보 */

    private String recipient;   //받는 사람 이름
    private String recipientAddress;     //배달 주소
    private String recipientPhone;   //받는 사람 전화번호
    private String deliveryStatus;  //배달 상태
    private String deliveryMessage; //배달 메세지

    private LocalDateTime regDate;  //주문 날짜 및 시간
    private LocalDateTime modDate;  //주문 수정 날짜 및 시간
    private Long pointAmountToBuyer;     //결과 정보
}
