package com.reborn.golf.usedtrade.service;

import com.reborn.golf.category.entity.Category;
import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.member.entity.Member;
import com.reborn.golf.common.dto.ImageDto;
import com.reborn.golf.usedtrade.dto.UsedTradeDto;
import com.reborn.golf.usedtrade.entity.UsedTrade;
import com.reborn.golf.usedtrade.entity.UsedTradeImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface UsedTradeService {

    PageResultDto<Object[], UsedTradeDto> getListWithUser(Integer memberIdx, PageRequestDto requestDto);
    PageResultDto<Object[], UsedTradeDto> getList(PageRequestDto requestDto);

    UsedTradeDto read(Long purchasedItemsIdx);

    UsedTradeDto register(Integer memberIdx, Integer categoryIdx, UsedTradeDto usedTradeDto);

    Long modify(Integer memberIdx, Integer categoryIdx, UsedTradeDto usedTradeDto);

    Map<String, Object> modifyStep(Long purchasedProductIdx, Set<String> roleSet, Integer cost);

    void remove(Integer memberIdx, Long purchasedItemsIdx);

    default Map<String, Object> dtoToEntities(Member member, Category category, UsedTradeDto itemsDto) {

        Map<String, Object> entityMap = new HashMap<>();

        UsedTrade usedTrade = UsedTrade.builder()
                .idx(itemsDto.getIdx())
                .catagory(category)
                .brand(itemsDto.getBrand())
                .name(itemsDto.getName())
                .state(itemsDto.getState())
                .price(itemsDto.getPrice())
                .quantity(itemsDto.getQuantity())
                .details(itemsDto.getDetails())
                .address(itemsDto.getAddress())
                .member(member)
                .canceled(itemsDto.isCanceled())
                .build();

        entityMap.put("purchasedProduct", usedTrade);

        List<ImageDto> imageDtoList = itemsDto.getImageDtoList();
        //ProductImageDto 처리
        if (imageDtoList != null && imageDtoList.size() > 0) {
            List<UsedTradeImage> usedTradeImageList = imageDtoList.stream().map(imgDto -> UsedTradeImage.builder()
                    .path(imgDto.getPath())
                    .imgName(imgDto.getImgName())
                    .uuid(imgDto.getUuid())
                    .usedTrade(usedTrade)
                    .build()
            ).collect(Collectors.toList());

            entityMap.put("imgList", usedTradeImageList);
        }
        return entityMap;
    }

    default UsedTradeDto entitiesToDto(UsedTrade items, List<UsedTradeImage> usedTradeImageList, Member member) {

        List<ImageDto> imageDtoList = usedTradeImageList.stream().map(productImage ->
                ImageDto.builder()
                        .imgName(productImage.getImgName())
                        .path(productImage.getPath())
                        .uuid(productImage.getUuid())
                        .build()).collect(Collectors.toList());

        return UsedTradeDto.builder()
                .idx(items.getIdx())
                .catagory(items.getCatagory().getName())
                .state(items.getState())
                .price(items.getPrice())
                .quantity(items.getQuantity())
                .address(items.getAddress())
                .brand(items.getBrand())
                .name(items.getName())
                .details(items.getDetails())
                .acceptedPrice(items.getAcceptedPrice())
                .acceptedTokenPrice(items.getAcceptedTokenPrice())
                .acceptedTokenAmount(items.getAcceptedTokenAmount())
                .canceled(items.isCanceled())
                .step(items.getUsedTradeStep().name())
                .memberEmail(member.getEmail())
                .memberName(member.getName())
                .regDate(items.getRegDate())
                .modDate(items.getModDate())
                .imageDtoList(imageDtoList)
                .build();
    }

    default UsedTradeDto entitiesToDto(UsedTrade items, List<UsedTradeImage> usedTradeImageList, String categoryName) {

        List<ImageDto> imageDtoList = usedTradeImageList.stream().map(productImage ->
                ImageDto.builder()
                        .imgName(productImage.getImgName())
                        .path(productImage.getPath())
                        .uuid(productImage.getUuid())
                        .build()).collect(Collectors.toList());

        return UsedTradeDto.builder()
                .idx(items.getIdx())
                .catagory(categoryName)
                .brand(items.getBrand())
                .name(items.getName())
                .state(items.getState())
                .price(items.getPrice())
                .quantity(items.getQuantity())
                .address(items.getAddress())
                .details(items.getDetails())
                .canceled(items.isCanceled())
                .step(items.getUsedTradeStep().name())
                .imageDtoList(imageDtoList)
                .build();
    }
}
