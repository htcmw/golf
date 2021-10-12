package com.reborn.golf.service;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.shop.PurchasedProductDto;
import com.reborn.golf.entity.PurchasedProduct;
import com.reborn.golf.repository.PurchasedProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;


@Log4j2
@RequiredArgsConstructor
@Service
public class PurchasedProductServiceImpl implements PurchasedProductService {

    private final PurchasedProductRepository purchasedProductRepository;

    @Override
    public PageResultDto<PurchasedProduct, PurchasedProductDto> getList(Integer memberIdx, PageRequestDto requestDto) {
        Page<PurchasedProduct> result = purchasedProductRepository.getPurchasedItemsbyMemberIdx(memberIdx, requestDto.getPageable(Sort.by("idx").descending()));
        Function<PurchasedProduct, PurchasedProductDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    public PurchasedProductDto read(Integer memberIdx, Long idx) {
        Optional<PurchasedProduct> result = purchasedProductRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, idx);

        if(result.isPresent()){
            PurchasedProduct purchasedProduct = result.get();
            return entityToDto(purchasedProduct);
        }
        return null;
    }

    @Override
    public Long register(Integer memberIdx, PurchasedProductDto purchasedProductDto) {
        PurchasedProduct purchasedProduct = dtoToEntity(memberIdx, purchasedProductDto);
        purchasedProductRepository.save(purchasedProduct);
        return purchasedProduct.getIdx();
    }

    @Override
    public Long modify(Integer memberIdx, PurchasedProductDto purchasedProductDto) {
        Optional<PurchasedProduct> result = purchasedProductRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, purchasedProductDto.getIdx());

        if(result.isPresent()){
            PurchasedProduct purchasedProduct = result.get();
            purchasedProduct.changeCatagory(purchasedProductDto.getCatagory());
            purchasedProduct.changeBrand(purchasedProductDto.getBrand());
            purchasedProduct.changeName(purchasedProductDto.getName());
            purchasedProduct.changeQuentity(purchasedProductDto.getQuentity());
            purchasedProduct.changeState(purchasedProductDto.getState());
            purchasedProduct.changePrice(purchasedProductDto.getPrice());
            purchasedProduct.changeDetails(purchasedProductDto.getDetails());
            return purchasedProduct.getIdx();
        }
        return null;
    }

    @Override
    public Long remove(Integer memberIdx, Long idx) {
        Optional<PurchasedProduct> result = purchasedProductRepository.getPurchasedItembyIdxAndMemberIdx(memberIdx, idx);

        if(result.isPresent()){
            PurchasedProduct purchasedProduct = result.get();
            purchasedProduct.changeRemoved(true);
            purchasedProductRepository.save(purchasedProduct);
            return purchasedProduct.getIdx();
        }
        return null;
    }
}
