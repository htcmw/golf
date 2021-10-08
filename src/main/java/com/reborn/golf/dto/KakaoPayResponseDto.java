package com.reborn.golf.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoPayResponseDto {
    private String tid;
//    private String ext_redirect_app_url;
//    private String next_redirect_mobile_url;//mockup-pg-web.kakao.com/v1/xxxxxxxxxx/mInfo",
    private String next_redirect_pc_url;//mockup-pg-web.kakao.com/v1/xxxxxxxxxx/info",
//    private String android_app_scheme;//kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order",
//    private String ios_app_scheme;//kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order",
    private String created_at;
}
