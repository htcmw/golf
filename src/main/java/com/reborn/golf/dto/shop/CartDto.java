package com.reborn.golf.dto.shop;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

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
