package com.reborn.golf.controller;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.shop.OrdersDto;
import com.reborn.golf.entity.Orders;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.OrderService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 유저의 주문 리스트 불러오기
    @GetMapping
    public ResponseEntity<PageResultDto<Orders, OrdersDto>> getListWithUser(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PageRequestDto pageRequestDto) {
        Integer memberIdx = authMemeberDto.getIdx();
        PageResultDto<Orders, OrdersDto> orderDtoList = orderService.getListWithUser(memberIdx, pageRequestDto);
        log.info(orderDtoList);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    // 주문 하기
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<OrdersDto> order(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody OrdersDto ordersDto) {
        Integer memberIdx = authMemeberDto.getIdx();
        OrdersDto orderInfo = orderService.order(memberIdx, ordersDto);
        return new ResponseEntity<>(orderInfo, HttpStatus.OK);
    }

    //주문 취소하기
    @DeleteMapping("/{idx}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> cancel(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx) {
        Integer memberIdx = authMemeberDto.getIdx();
        Long num = orderService.cancel(memberIdx, idx);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //어떤 상태의 주문 정보리스트 불러오기
    @GetMapping("/state")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<PageResultDto<Orders, OrdersDto>> getProcessingOrder(@RequestParam String state, PageRequestDto pageRequestDto) {
        PageResultDto<Orders, OrdersDto> orderDtoList = orderService.getListWithState(state, pageRequestDto);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

}
