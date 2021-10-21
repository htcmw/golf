package com.reborn.golf.controller;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.shop.PurchasedProductDto;
import com.reborn.golf.entity.Enum.Role;
import com.reborn.golf.entity.PurchasedProduct;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.PurchasedProductService;
import jnr.ffi.annotations.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@RequestMapping("/purchased-products")
@RestController
public class PurchasedProductController {

    private final PurchasedProductService purchasedProductService;

    //유저의 판매 리스트
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<PageResultDto<Object[], PurchasedProductDto>> getListWithUser(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PageRequestDto requestDto) {
        PageResultDto<Object[], PurchasedProductDto> purchasedItemsDtoList = purchasedProductService.getListWithUser(authMemeberDto.getIdx(), requestDto);
        log.info(purchasedItemsDtoList);
        return new ResponseEntity<>(purchasedItemsDtoList, HttpStatus.OK);
    }

    //유저의 물품 판매 정보 확인
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{idx}")
    public ResponseEntity<PurchasedProductDto> read(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx) {
        PurchasedProductDto purchasedProductDto = purchasedProductService.read(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>(purchasedProductDto, HttpStatus.OK);
    }

    //중고 용품 등록
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<PurchasedProductDto> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                                        @RequestParam Integer categoryIdx,
                                                        @RequestBody PurchasedProductDto purchasedProductDto) {
        PurchasedProductDto register = purchasedProductService.register(authMemeberDto.getIdx(), categoryIdx, purchasedProductDto);
        return new ResponseEntity<>(register, HttpStatus.OK);
    }

    //판매 정보 수정
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                       @RequestParam Integer categoryIdx,
                                       @RequestBody PurchasedProductDto purchasedProductDto) {
        Long num = purchasedProductService.modify(authMemeberDto.getIdx(), categoryIdx, purchasedProductDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //판매 취소
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx) {
        purchasedProductService.remove(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>("purchasedProduct is removed successfully", HttpStatus.OK);
    }

    //판매 상태 변경
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @PutMapping("/{idx}/step")
    public ResponseEntity<Map<String, Object>> modifyState(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                                           @PathVariable Long idx,
                                                           @RequestBody(required = false) Map<String, Integer> param) {
        Integer cost = null;
        if (param != null)
            cost = param.get("cost");
        Collection<GrantedAuthority> authorities = authMemeberDto.getAuthorities();
        Set<String> roleSet = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        Map<String, Object> map = purchasedProductService.modifyStep(idx, roleSet, cost);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
