package com.reborn.golf.dto.kakaopay;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoPayResponseDto {
    private String partnerOrderId;
    private String partnerUserId;
    private String tid;
    private Integer totalAmount;
    private String created_at;
    private String next_redirect_pc_url;//mockup-pg-web.kakao.com/v1/xxxxxxxxxx/info",
}
