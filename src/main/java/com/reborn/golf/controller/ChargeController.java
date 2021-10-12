package com.reborn.golf.controller;


import com.reborn.golf.dto.kakaopay.*;
import com.reborn.golf.service.ChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/payments")
@Log4j2
@RequiredArgsConstructor
public class ChargeController {

    private final ChargeService chargeService;


    @PostMapping("/kakaopay/ready")
    public ResponseEntity<KakaoPayResponseDto> readyPayment(String partnerOrderId, String partnerUserId, String itemName, Integer quantity, Integer totalAmount){

        log.info("partnerOrderId = " + partnerOrderId +
                "\npartnerUserId : " + partnerUserId +
                "\nitemName : " + itemName +
                "\nquentity : " + quantity +
                "\ntotalAmount : " +totalAmount);

        KakaoPayResponseDto kakaoPayResponseDto = null;
        try {
            kakaoPayResponseDto = chargeService.readyPayment(partnerOrderId, partnerUserId, itemName, quantity, totalAmount, 0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(kakaoPayResponseDto, HttpStatus.OK);
    }

    @GetMapping("/kakaopay/approval/{orderId}")
    public void kakaoPaymentApprove(@PathVariable String orderId, @RequestParam("pg_token") String pgToken){
        KakaoPayApprovalDto kakaoPayApprovalDto = chargeService.approvePayment(orderId, pgToken);
        log.info(kakaoPayApprovalDto);
    }


    @PostMapping("/kakaopay/cancel/{orderId}")
    public ResponseEntity<KakaoPayCancelResponseDto> cancelPayment(@PathVariable String orderId){

        log.info("=============@PostMapping(\"/kakaopay/cancel/{orderId}\")===========================");
        KakaoPayCancelResponseDto kakaoPayCancelResponseDto = null;
        try {
            kakaoPayCancelResponseDto = chargeService.cancelPayment(orderId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(kakaoPayCancelResponseDto, HttpStatus.OK);
    }

    @PostMapping("/kakaopay/fail")
    public void failPayment(){

        log.info("=============@PostMapping(\"/kakaopay/fail\")===========================");
    }

}
