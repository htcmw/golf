package com.reborn.golf.controller;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.shop.OrdersDto;
import com.reborn.golf.entity.Orders;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 정보 불러오기
    @GetMapping
    public ResponseEntity<PageResultDto<Orders, OrdersDto>> getListWithUser(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PageRequestDto pageRequestDto) {
        Integer memberIdx = authMemeberDto.getIdx();

        PageResultDto<Orders, OrdersDto> orderDtoList = orderService.getListWithUser(memberIdx, pageRequestDto);
        log.info(orderDtoList);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    // 주문 정보 불러오기
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> order(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody OrdersDto ordersDto) {
        Integer memberIdx = authMemeberDto.getIdx();
        log.info("here");
        String url = orderService.order(memberIdx, ordersDto);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> cancel(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx) {
        Integer memberIdx = authMemeberDto.getIdx();
        Long num = orderService.cancel(memberIdx, idx);
        log.info(num);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    @GetMapping("/permit")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<PageResultDto<Orders, OrdersDto>> getProcessingOrder(PageRequestDto pageRequestDto) {
        PageResultDto<Orders, OrdersDto> orderDtoList = orderService.getList(pageRequestDto);
        log.info(orderDtoList);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

}
