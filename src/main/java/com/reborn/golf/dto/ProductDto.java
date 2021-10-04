package com.reborn.golf.dto;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long pno;

    private String catagory;

    private String title;

    private String brand;

    private String rank;

    private int quantity;

    private int price;

    private String content;

    // 이미지 생성
    @Builder.Default
    private List<ProductImageDto> imageDtoList = new ArrayList<>();

    //영화의 평균 평점
    private double avg;

    //리뷰 수 jpa의 count( )
    private int reviewCnt;

//    private LocalDateTime regDate;
//
//    private LocalDateTime modDate;

}
