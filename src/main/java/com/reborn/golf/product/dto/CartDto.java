package com.reborn.golf.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long cartIdx;

    private Integer totalPrice;

    private Integer quantity;

    private Long productIdx;

    //price per 1 product
    private Integer price;

    private String title;

    private ProductImageDto imageDtoList;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
