package com.reborn.golf.order.restcontroller;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.order.dto.OrdersDto;
import com.reborn.golf.order.entity.Orders;
import com.reborn.golf.order.service.OrderService;
import com.reborn.golf.security.dto.AuthMemeberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Log4j2
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService orderService;
    // 주문 하기
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<OrdersDto> order(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody OrdersDto ordersDto) {
        return ResponseEntity.ok(orderService.runPayment(authMemeberDto.getIdx(), ordersDto));
    }

    //주문 취소하기
    @DeleteMapping("/{idx}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> cancel(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx) {
        return ResponseEntity.ok(orderService.cancelAll(authMemeberDto.getIdx(), idx));
    }

    // 유저의 주문 리스트 불러오기
    @GetMapping("/user")
    public ResponseEntity<PageResultDto<Orders, OrdersDto>> getListWithUser(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(orderService.getListWithUser(authMemeberDto.getIdx(), pageRequestDto));
    }
}
