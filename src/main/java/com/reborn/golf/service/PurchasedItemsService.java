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

    PageResultDto<Object[], ProductDto> getList(Integer memberIdx, PageRequestDto requestDto);

    Long register(Integer writerIdx, ProductDto productDto);

    ProductDto read(Integer writerIdx, Long productIdx);

    Long modify(Integer writerIdx, ProductDto productDto);

    Long remove(Integer writerIdx, Long productIdx);

    default PurchasedItemsDto entityToDto(PurchasedItems items,  Member member) {
        return PurchasedItemsDto.builder()
                .idx(items.getIdx())
                .catagory(items.getCatagory())
                .name(items.getName())
                .brand(items.getBrand())
                .canceled(items.isCanceled())
                .details(items.getDetails())
                .finished(items.isFinished())
                .imageDtoList(items.getPurchasedItemsImages())
                .memberEmail(member.getEmail())
                .memberName(member.getName())
                .build();
    }
}
