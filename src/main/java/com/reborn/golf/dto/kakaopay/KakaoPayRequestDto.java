package com.reborn.golf.dto.kakaopay;

import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoPayRequestDto {
    String partnerOrderId;
    String partnerUserId;
    String title;
    Integer totalAmount;
}
