package com.reborn.golf.test.kakaopay;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class KakaoPayCancelResponseDto {
    //요청 고유 번호
    private String aid;
    //결제 고유 번호, 10자
    private String tid;
    //가맹점 코드, 20자
    private String cid;
    //결제 상태
    private String status;
    //가맹점 주문번호, 최대 100자
    private String partner_order_id;
    //가맹점 회원 id, 최대 100자
    private String partner_user_id;
    //결제 수단, CARD 또는 MONEY 중 하나
    private String payment_method_type;
    //결제 금액 정보
    private AmountDto amount;
    //이번 요청으로 취소된 금액
    private AmountDto approved_cancel_amount;
    //누계 취소 금액
    private AmountDto canceled_amount;
    //남은 취소 가능 금액
    private AmountDto cancel_available_amount;
    //상품 이름, 최대 100자
    private String item_name;
    //상품 코드, 최대 100자
    private String item_code;
    //상품 수량
    private Integer quantity;
    //결제 준비 요청 시각
    private Date created_at;
    //결제 승인 시각
    private Date approved_at;
    //결제 취소 시각
    private Date canceled_at;
    //	취소 요청 시 전달한 값
    private String payload;
}
