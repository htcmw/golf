package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.ProductDto;
import com.reborn.golf.dto.PurchasedItemsDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.PurchasedItems;
import com.reborn.golf.entity.PurchasedItemsImage;

import java.util.List;

public interface PurchasedItemsService {

    PageResultDto<PurchasedItems, PurchasedItemsDto> getList(Integer memberIdx, PageRequestDto requestDto);

    Long register(Integer memberIdx, PurchasedItemsDto purchasedItemsDto);

    PurchasedItemsDto read(Integer memberIdx, Long purchasedItemsIdx);

    Long modify(Integer memberIdx, PurchasedItemsDto purchasedItemsDto);

    Long remove(Integer memberIdx, Long purchasedItemsIdx);

    default PurchasedItems dtoToEntity(Integer memberIdx, PurchasedItemsDto itemsDto){
        return PurchasedItems.builder()
                .idx(itemsDto.getIdx())
                .catagory(itemsDto.getCatagory())
                .name(itemsDto.getName())
                .brand(itemsDto.getBrand())
                .state(itemsDto.getState())
                .price(itemsDto.getPrice())
                .quentity(itemsDto.getQuentity())
                .details(itemsDto.getDetails())
                .member(Member.builder().idx(memberIdx).build())
                .purchasedItemsImages(itemsDto.getImageDtoList())
                .canceled(itemsDto.isCanceled())
                .finished(itemsDto.isFinished())
                .build();
    }

    default PurchasedItemsDto entityToDto(PurchasedItems items) {
        return PurchasedItemsDto.builder()
                .idx(items.getIdx())
                .catagory(items.getCatagory())
                .name(items.getName())
                .brand(items.getBrand())
                .canceled(items.isCanceled())
                .details(items.getDetails())
                .finished(items.isFinished())
                .imageDtoList(items.getPurchasedItemsImages())
                .memberEmail(items.getMember().getEmail())
                .memberName(items.getMember().getName())
                .build();
    }

}
