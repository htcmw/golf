package com.reborn.golf.test.kakaopay;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class KakaoPayApprovalDto {
    //요청 고유 번호
    private String aid;
    //결제 고유 번호, 10자
    private String tid;
    //가맹점 코드, 20자
    private String cid;
    //정기결제용 ID, 정기결제 CID로 단건결제 요청 시 발급
    private String sid;
    //가맹점 주문번호, 최대 100자
    private String partner_order_id;
    //가맹점 회원 id, 최대 100자
    private String partner_user_id;
    //결제 수단, CARD 또는 MONEY 중 하나
    private String payment_method_type;
    //결제 금액 정보
    private AmountDto amount;
    //결제 상세 정보, 결제수단이 카드일 경우만 포함
    private CardInfoDto card_info;
    //상품 이름, 최대 100자
    private String item_name;
    //상품 코드, 최대 100자
    private String item_code;
    //상품 수량
    private Integer quantity;
    //결제 준비 요청 시각
    private LocalDateTime created_at;
    //결제 승인 시각
    private LocalDateTime approved_at;
    //결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
    private String payload;
}
