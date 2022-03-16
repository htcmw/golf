package com.reborn.golf.usedtrade.dto;

import com.reborn.golf.common.dto.ImageDto;
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
    private String brand;   //물품 브랜드
    private String name;    //물품 이름
    private String state;   //물품상태
    private Integer price;  //물품 희망 가격
    private Integer quantity;   //수량
    private String details; //설명
    private String address; //주소

    /* Response에서만 사용됨 */
    private String catagory;    //물품 종류
    private String memberEmail; //유저정보
    private String memberName;  //물품 구입 처리 단계
    private String step;    //유저가 원하는 가격에 대한 비용
    private Integer expectedPrice;  //유저가 원하는 가격에 대한 토큰 양
    private Long expectedPointAmount;   //회사가 구입할 수 있는 가격
    private Integer proposalPrice;  //회사가 구입할 수 있는 토큰 수량
    private Long proposalTokenAmount;   //회사가 구입한 가격
    private Integer acceptedPrice;
    private Long acceptedTokenAmount;   //회사가 구입할 때 토큰 수량
    private String acceptedTokenPrice;  //회사가 구입할 때 토큰 시장 가격
    private boolean canceled;
    @Builder.Default
    private List<ImageDto> imageDtoList = new ArrayList<>(); // 이미지 생성

    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
