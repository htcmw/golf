package com.reborn.golf.service;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.shop.ProductImageDto;
import com.reborn.golf.dto.shop.PurchasedProductDto;
import com.reborn.golf.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface PurchasedProductService {

    PageResultDto<Object[], PurchasedProductDto> getListWithUser(Integer memberIdx, PageRequestDto requestDto);

    PurchasedProductDto read(Integer memberIdx, Long purchasedItemsIdx);

    PurchasedProductDto register(Integer memberIdx, Integer categoryIdx, PurchasedProductDto purchasedProductDto);

    Long modify(Integer memberIdx, Integer categoryIdx, PurchasedProductDto purchasedProductDto);

    Map<String, Object> modifyStep(Long purchasedProductIdx, Set<String> roleSet, Integer cost);

    void remove(Integer memberIdx, Long purchasedItemsIdx);

    default Map<String, Object> dtoToEntities(Member member, Category category, PurchasedProductDto itemsDto) {

        Map<String, Object> entityMap = new HashMap<>();

        PurchasedProduct purchasedProduct = PurchasedProduct.builder()
                .idx(itemsDto.getIdx())
                .catagory(category)
                .brand(itemsDto.getBrand())
                .name(itemsDto.getName())
                .state(itemsDto.getState())
                .price(itemsDto.getPrice())
                .quentity(itemsDto.getQuentity())
                .details(itemsDto.getDetails())
                .address(itemsDto.getAddress())
                .member(member)
                .canceled(itemsDto.isCanceled())
                .build();

        entityMap.put("purchasedProduct", purchasedProduct);

        List<ProductImageDto> imageDtoList = itemsDto.getImageDtoList();
        //ProductImageDto 처리
        if (imageDtoList != null && imageDtoList.size() > 0) {
            List<PurchasedProductImage> purchasedProductImageList = imageDtoList.stream().map(imgDto -> PurchasedProductImage.builder()
                    .path(imgDto.getPath())
                    .imgName(imgDto.getImgName())
                    .uuid(imgDto.getUuid())
                    .purchasedProduct(purchasedProduct)
                    .build()
            ).collect(Collectors.toList());

            entityMap.put("imgList", purchasedProductImageList);
        }
        return entityMap;
    }

    default PurchasedProductDto entitiesToDto(PurchasedProduct items, List<PurchasedProductImage> purchasedProductImageList, Member member) {

        List<ProductImageDto> productImageDtoList = purchasedProductImageList.stream().map(productImage ->
                ProductImageDto.builder()
                        .imgName(productImage.getImgName())
                        .path(productImage.getPath())
                        .uuid(productImage.getUuid())
                        .build()).collect(Collectors.toList());

        return PurchasedProductDto.builder()
                .idx(items.getIdx())
                .catagory(items.getCatagory().getName())
                .state(items.getState())
                .price(items.getPrice())
                .quentity(items.getQuentity())
                .address(items.getAddress())
                .brand(items.getBrand())
                .name(items.getName())
                .details(items.getDetails())
                .canceled(items.isCanceled())
                .step(items.getPurchasedProductStep().name())
                .memberEmail(member.getEmail())
                .memberName(member.getName())
                .regDate(items.getRegDate())
                .modDate(items.getModDate())
                .imageDtoList(productImageDtoList)
                .build();
    }

    default PurchasedProductDto entitiesToDto(PurchasedProduct items, List<PurchasedProductImage> purchasedProductImageList, String categoryName) {

        List<ProductImageDto> productImageDtoList = purchasedProductImageList.stream().map(productImage ->
                ProductImageDto.builder()
                        .imgName(productImage.getImgName())
                        .path(productImage.getPath())
                        .uuid(productImage.getUuid())
                        .build()).collect(Collectors.toList());

        return PurchasedProductDto.builder()
                .idx(items.getIdx())
                .catagory(categoryName)
                .brand(items.getBrand())
                .name(items.getName())
                .state(items.getState())
                .price(items.getPrice())
                .quentity(items.getQuentity())
                .address(items.getAddress())
                .details(items.getDetails())
                .canceled(items.isCanceled())
                .step(items.getPurchasedProductStep().name())
                .imageDtoList(productImageDtoList)
                .build();
    }
}
