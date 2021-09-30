package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.ProductDto;
import com.reborn.golf.dto.PurchasedItemsDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.PurchasedItems;
import com.reborn.golf.entity.PurchasedItemsImage;
import com.reborn.golf.repository.PurchasedItemsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;


@Log4j2
@RequiredArgsConstructor
public class PurchasedItemsServiceImpl implements PurchasedItemsService {
    private final PurchasedItemsRepository purchasedItemsRepository;

    @Override
    public PageResultDto<Object[], PurchasedItemsDto> getList(Integer memberIdx, PageRequestDto requestDto) {
        Page<Object[]> result = purchasedItemsRepository.getPurchasedItemsbyMemberIdx(memberIdx, requestDto.getPageable(Sort.by("idx").descending()));
        Function<Object[], PurchasedItemsDto> function = (entities -> entityToDto((PurchasedItems) entities[0], (Member) entities[1]));
        return new PageResultDto<>(result, function);
    }

    @Override
    public Long register(Integer writerIdx, ProductDto productDto) {
        return null;
    }

    @Override
    public ProductDto read(Integer writerIdx, Long productIdx) {
        return null;
    }

    @Override
    public Long modify(Integer writerIdx, ProductDto productDto) {
        return null;
    }

    @Override
    public Long remove(Integer writerIdx, Long productIdx) {
        return null;
    }
}
