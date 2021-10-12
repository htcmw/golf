package com.reborn.golf.dto.kakaopay;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoPayResponseDto {
    //결제 고유번호
    private String tid;
    //결제 준비 요청 시각
    private LocalDateTime created_at;

    private String next_redirect_pc_url;
}
