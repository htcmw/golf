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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDto cartDto = (CartDto) o;
        return Objects.equals(cartIdx, cartDto.cartIdx) && Objects.equals(totalPrice, cartDto.totalPrice) && Objects.equals(quantity, cartDto.quantity) && Objects.equals(productIdx, cartDto.productIdx) && Objects.equals(price, cartDto.price) && Objects.equals(title, cartDto.title) && Objects.equals(imageDtoList, cartDto.imageDtoList) && Objects.equals(regDate, cartDto.regDate) && Objects.equals(modDate, cartDto.modDate);
    }

}
