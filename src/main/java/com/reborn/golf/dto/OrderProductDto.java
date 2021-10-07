package com.reborn.golf.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderProductDto {
    private Long orderProductIdx;

    private Long productIdx;

    private Integer quentity;

    private Integer price;

}
