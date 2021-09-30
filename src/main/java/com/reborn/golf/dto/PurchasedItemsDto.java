package com.reborn.golf.dto;

import com.reborn.golf.entity.PurchasedItemsImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasedItemsDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String catagory;

    private String name;

    private String brand;

    private String state;

    private int price;

    private int quentity;

    private String details;

    private String memberEmail;

    private String memberName;

    // 이미지 생성
    @Builder.Default
    private List<PurchasedItemsImage> imageDtoList = new ArrayList<>();

    private boolean finished;

    private boolean canceled;



}
