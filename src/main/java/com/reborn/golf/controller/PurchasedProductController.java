package com.reborn.golf.controller;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.shop.PurchasedProductDto;
import com.reborn.golf.entity.PurchasedProduct;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.PurchasedProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/sales")
@Log4j2
@RequiredArgsConstructor
public class PurchasedProductController {

    private final PurchasedProductService purchasedProductService;


    //유저의 판매 리스트
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<PageResultDto<PurchasedProduct, PurchasedProductDto>> getList(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PageRequestDto requestDto ){
        PageResultDto<PurchasedProduct, PurchasedProductDto> purchasedItemsDtoList =  purchasedProductService.getList(authMemeberDto.getIdx(), requestDto);
        return new ResponseEntity<>(purchasedItemsDtoList, HttpStatus.OK);
    }

    //유저의 판매정보 확인
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{idx}")
    public ResponseEntity<PurchasedProductDto> read(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        PurchasedProductDto purchasedProductDto = purchasedProductService.read(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>(purchasedProductDto, HttpStatus.OK);
    }

    //중고 용품 등록
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PurchasedProductDto purchasedProductDto){
        Long num = purchasedProductService.register(authMemeberDto.getIdx(), purchasedProductDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //판매 정보 수정
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PurchasedProductDto purchasedProductDto){
        Long num = purchasedProductService.modify(authMemeberDto.getIdx(), purchasedProductDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //판매 취소
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        Long num = purchasedProductService.remove(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/{idx}/acceptance")
    public ResponseEntity<PurchasedProductDto> accept(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        PurchasedProductDto purchasedProductDto = purchasedProductService.read(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>(purchasedProductDto, HttpStatus.OK);
    }
    //판매 상태 변경
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/states")
    public ResponseEntity<Long> modifyState(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PurchasedProductDto purchasedProductDto){
        Long num = purchasedProductService.modify(authMemeberDto.getIdx(), purchasedProductDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

}
