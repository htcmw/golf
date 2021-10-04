//package com.reborn.golf.controller;
//
//import com.reborn.golf.dto.*;
//import com.reborn.golf.service.OrderService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/order")
//@Log4j2
//@RequiredArgsConstructor
//public class OrderController {
//
//    private final OrderService orderService;
//
//    // 주문 정보 불러오기
//    @GetMapping
//    public ResponseEntity<PageResultDto<Object[], OrderDto>> getList(PageRequestDto pageRequestDto) {
//        PageResultDto<Object[], OrderDto> orderDtoList = orderService.getList(pageRequestDto);
//        log.info(orderDtoList);
//        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
//    }
//    // 배송지 정보 불러오기
//
//    //
//}
