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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;

@RequestMapping("/sales")
@Log4j2
@RequiredArgsConstructor
public class PurchasedItemsController {

    private final PurchasedItemsService purchasedItemsService;

    @GetMapping
    public ResponseEntity<PageResultDto<PurchasedItems, PurchasedItemsDto>> getList(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PageRequestDto requestDto ){
        PageResultDto<PurchasedItems, PurchasedItemsDto> purchasedItemsDtoList =  purchasedItemsService.getList(authMemeberDto.getIdx(), requestDto);
        return new ResponseEntity<>(purchasedItemsDtoList, HttpStatus.OK);
    }


    @GetMapping("/{idx}")
    public ResponseEntity<PurchasedItemsDto> read(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        PurchasedItemsDto purchasedItemsDto = purchasedItemsService.read(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>(purchasedItemsDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PurchasedItemsDto purchasedItemsDto){
        Long num = purchasedItemsService.register(authMemeberDto.getIdx(), purchasedItemsDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PurchasedItemsDto purchasedItemsDto){
        Long num = purchasedItemsService.modify(authMemeberDto.getIdx(), purchasedItemsDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        Long num = purchasedItemsService.remove(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }
}
