package com.reborn.golf.test.kakaopay;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AmountDto {
    //전체 결제 금액
    private Integer total;
    //비과세 금액
    private Integer tax_free;
    //부가세 금액
    private Integer vat;
    //사용한 포인트 금액
    private Integer point;
    //할인 금액
    private Integer discount;
}
