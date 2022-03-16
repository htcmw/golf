package com.reborn.golf.order.dto;

import com.reborn.golf.common.dto.ImageDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderProductDto {
    private Long orderProductIdx;
    private Integer price;
    private Integer quantity;

    private Long productIdx;
    private String title;
    private String brand;
    private String rank;
    private String content;
    private Boolean isRemoved;

    @Builder.Default
    private List<ImageDto> imageDtoList = new ArrayList<>();

}
