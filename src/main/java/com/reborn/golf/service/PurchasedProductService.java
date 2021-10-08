package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.PurchasedProductDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.PurchasedProduct;

public interface PurchasedProductService {

    PageResultDto<PurchasedProduct, PurchasedProductDto> getList(Integer memberIdx, PageRequestDto requestDto);

    Long register(Integer memberIdx, PurchasedProductDto purchasedProductDto);

    PurchasedProductDto read(Integer memberIdx, Long purchasedItemsIdx);

    Long modify(Integer memberIdx, PurchasedProductDto purchasedProductDto);

    Long remove(Integer memberIdx, Long purchasedItemsIdx);

    default PurchasedProduct dtoToEntity(Integer memberIdx, PurchasedProductDto itemsDto){
        return PurchasedProduct.builder()
                .idx(itemsDto.getIdx())
                .catagory(itemsDto.getCatagory())
                .name(itemsDto.getName())
                .brand(itemsDto.getBrand())
                .state(itemsDto.getState())
                .price(itemsDto.getPrice())
                .quentity(itemsDto.getQuentity())
                .details(itemsDto.getDetails())
                .member(Member.builder().idx(memberIdx).build())
                .purchasedProductImages(itemsDto.getImageDtoList())
                .canceled(itemsDto.isCanceled())
                .finished(itemsDto.isFinished())
                .build();
    }

    default PurchasedProductDto entityToDto(PurchasedProduct items) {
        return PurchasedProductDto.builder()
                .idx(items.getIdx())
                .catagory(items.getCatagory())
                .name(items.getName())
                .brand(items.getBrand())
                .canceled(items.isCanceled())
                .details(items.getDetails())
                .finished(items.isFinished())
                .imageDtoList(items.getPurchasedProductImages())
                .memberEmail(items.getMember().getEmail())
                .memberName(items.getMember().getName())
                .build();
    }

}
