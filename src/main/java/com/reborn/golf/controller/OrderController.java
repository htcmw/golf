package com.reborn.golf.controller;

import com.reborn.golf.dto.*;
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
    public ResponseEntity<PageResultDto<Object[], OrderDto>> getListWithUser(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PageRequestDto pageRequestDto) {
        Integer memberIdx = authMemeberDto.getIdx();

        PageResultDto<Object[], OrderDto> orderDtoList = orderService.getListWithUser(memberIdx, pageRequestDto);
        log.info(orderDtoList);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    // 주문 정보 불러오기
    @PostMapping
    public ResponseEntity<Long> order(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, OrderDto orderDto) {
        Integer memberIdx = authMemeberDto.getIdx();
        Long idx = orderService.order(memberIdx, orderDto);
        log.info(idx);
        return new ResponseEntity<>(idx, HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> cancel(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long orderIdx) {
        Integer memberIdx = authMemeberDto.getIdx();
        Long idx = orderService.cancel(memberIdx, orderIdx);
        log.info(idx);
        return new ResponseEntity<>(idx, HttpStatus.OK);
    }


    @GetMapping("/permit")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<PageResultDto<Object[], OrderDto>> getProcessingOrder(PageRequestDto pageRequestDto) {
        PageResultDto<Object[], OrderDto> orderDtoList = orderService.getList(pageRequestDto);
        log.info(orderDtoList);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

}
