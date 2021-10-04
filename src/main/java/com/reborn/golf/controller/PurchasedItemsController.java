package com.reborn.golf.controller;

import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.PurchasedItemsDto;
import com.reborn.golf.entity.PurchasedItems;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.PurchasedItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;

@RequestMapping("/sales")
@Log4j2
@RequiredArgsConstructor
public class PurchasedItemsController {

    private final PurchasedItemsService purchasedItemsService;


    //유저의 판매 리스트
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<PageResultDto<PurchasedItems, PurchasedItemsDto>> getList(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PageRequestDto requestDto ){
        PageResultDto<PurchasedItems, PurchasedItemsDto> purchasedItemsDtoList =  purchasedItemsService.getList(authMemeberDto.getIdx(), requestDto);
        return new ResponseEntity<>(purchasedItemsDtoList, HttpStatus.OK);
    }

    //유저의 판매정보 확인
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{idx}")
    public ResponseEntity<PurchasedItemsDto> read(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        PurchasedItemsDto purchasedItemsDto = purchasedItemsService.read(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>(purchasedItemsDto, HttpStatus.OK);
    }

    //중고 용품 등록
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PurchasedItemsDto purchasedItemsDto){
        Long num = purchasedItemsService.register(authMemeberDto.getIdx(), purchasedItemsDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //판매 정보 수정
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PurchasedItemsDto purchasedItemsDto){
        Long num = purchasedItemsService.modify(authMemeberDto.getIdx(), purchasedItemsDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //판매 취소
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        Long num = purchasedItemsService.remove(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/{idx}/acceptance")
    public ResponseEntity<PurchasedItemsDto> accept(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        PurchasedItemsDto purchasedItemsDto = purchasedItemsService.read(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>(purchasedItemsDto, HttpStatus.OK);
    }
    //판매 상태 변경
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/states")
    public ResponseEntity<Long> modifyState(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PurchasedItemsDto purchasedItemsDto){
        Long num = purchasedItemsService.modify(authMemeberDto.getIdx(), purchasedItemsDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

}
