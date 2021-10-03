package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.PurchasedItemsDto;
import com.reborn.golf.entity.PurchasedItems;
import com.reborn.golf.repository.PurchasedItemsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.function.Function;


@Log4j2
@RequiredArgsConstructor
public class PurchasedItemsServiceImpl implements PurchasedItemsService {

    private final PurchasedItemsRepository purchasedItemsRepository;

    @Override
    public PageResultDto<PurchasedItems, PurchasedItemsDto> getList(Integer memberIdx, PageRequestDto requestDto) {
        Page<PurchasedItems> result = purchasedItemsRepository.getPurchasedItemsbyMemberIdx(memberIdx, requestDto.getPageable(Sort.by("idx").descending()));
        Function<PurchasedItems, PurchasedItemsDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    public PurchasedItemsDto read(Integer memberIdx, Long idx) {
        Optional<PurchasedItems> result = purchasedItemsRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, idx);

        if(result.isPresent()){
            PurchasedItems purchasedItems = result.get();
            return entityToDto(purchasedItems);
        }
        return null;
    }

    @Override
    public Long register(Integer memberIdx, PurchasedItemsDto purchasedItemsDto) {
        PurchasedItems purchasedItems = dtoToEntity(memberIdx, purchasedItemsDto);
        purchasedItemsRepository.save(purchasedItems);
        return purchasedItems.getIdx();
    }

    @Override
    public Long modify(Integer memberIdx, PurchasedItemsDto purchasedItemsDto) {
        Optional<PurchasedItems> result = purchasedItemsRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, purchasedItemsDto.getIdx());

        if(result.isPresent()){
            PurchasedItems purchasedItems = result.get();
            purchasedItems.changeCatagory(purchasedItemsDto.getCatagory());
            purchasedItems.changeBrand(purchasedItemsDto.getBrand());
            purchasedItems.changeName(purchasedItemsDto.getName());
            purchasedItems.changeQuentity(purchasedItemsDto.getQuentity());
            purchasedItems.changeState(purchasedItemsDto.getState());
            purchasedItems.changePrice(purchasedItemsDto.getPrice());
            purchasedItems.changeDetails(purchasedItemsDto.getDetails());
            return purchasedItems.getIdx();
        }
        return null;
    }

    @Override
    public Long remove(Integer memberIdx, Long idx) {
        Optional<PurchasedItems> result = purchasedItemsRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, idx);

        if(result.isPresent()){
            PurchasedItems purchasedItems = result.get();
            purchasedItems.changeRemoved(true);
            purchasedItemsRepository.save(purchasedItems);
            return purchasedItems.getIdx();
        }
        return null;
    }
}
