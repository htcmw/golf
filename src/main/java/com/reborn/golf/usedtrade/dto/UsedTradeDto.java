package com.reborn.golf.usedtrade.dto;

import com.reborn.golf.product.dto.ProductImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsedTradeDto {

    private Long idx;
    //물품 브랜드
    private String brand;
    //물품 이름
    private String name;
    //물품상태
    private String state;
    //물품 희망 가격
    private Integer price;
    //수량
    private Integer quantity;
    //설명
    private String details;
    //주소
    private String address;

    /*
    * Response에서만 사용됨
    * */
    //물품 종류
    private String catagory;
    //유저정보
    private String memberEmail;
    private String memberName;
    //물품 구입 처리 단계
    private String step;
    //유저가 원하는 가격에 대한 비용
    private Integer expectedPrice;
    //유저가 원하는 가격에 대한 토큰 양
    private Long expectedPointAmount;
    //회사가 구입할 수 있는 가격
    private Integer proposalPrice;
    //회사가 구입할 수 있는 토큰 수량
    private Long proposalTokenAmount;
    //회사가 구입한 가격
    private Integer acceptedPrice;
    //회사가 구입할 때 토큰 수량
    private Long acceptedTokenAmount;
    //회사가 구입할 때 토큰 시장 가격
    private String acceptedTokenPrice;
    // 이미지 생성
    @Builder.Default
    private List<ProductImageDto> imageDtoList = new ArrayList<>();

    private boolean canceled;

    private LocalDateTime regDate;
    private LocalDateTime modDate;


}
