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
    //포인트 금액
    private Double tokenPrice;
    //포인트 수량
    private Long tokenAmount;
    //주문 상태
    private String orderState;

    /*
    * orderProducts 정보
    * */
    //상품 종류 수, entity에 없음
    private Integer orderProductsCount;
    //구매 물품 및 수량
    private List<OrderProductDto> orderProductList;

    /*
     * 유저 정보
     * */
    //결제한 사람 이메일
    private String userEmail;
    //결제한 사람 이름
    private String userName;

    /*
    * 배달 정보
    * */
    //받는 사람 이름
    private String recipient;
    //배달 주소
    private String recipientAddress;
    //받는 사람 전화번호
    private String recipientPhone;
    //배달 상태
    private String deliveryStatus;
    //배달 메세지
    private String deliveryMessage;


    //주문 날짜 및 시간
    private LocalDateTime regDate;
    //주문 수정 날짜 및 시간
    private LocalDateTime modDate;

    //결과 정보
    private Long pointAmountToBuyer;
}
