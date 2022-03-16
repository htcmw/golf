package com.reborn.golf.product.dto;


import com.reborn.golf.common.dto.ImageDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long idx;
    private Integer categoryIdx;
    private String title;
    private String brand;
    private Integer quantity;
    private Integer price;
    private String content;
    private Integer salesVolume;
    // 이미지 생성
    private List<ImageDto> imageDtoList;
    private Double avg;
    private Long reviewCnt;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
