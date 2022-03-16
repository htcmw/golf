package com.reborn.golf.product.dto;


import com.reborn.golf.common.dto.ImageDto;
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
    private Integer price;  //price per 1 product
    private String title;
    private ImageDto imageDto;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

}
