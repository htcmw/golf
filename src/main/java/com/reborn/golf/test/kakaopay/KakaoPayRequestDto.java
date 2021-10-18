package com.reborn.golf.test.kakaopay;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoPayRequestDto {
    //가맹점 주문번호, 최대 100자
    String partnerOrderId;
    //가맹점 회원 id, 최대 100자
    String partnerUserId;
    //상품 이름, 최대 100자
    String itemName;
    //상품 총액
    Integer totalAmount;
}
