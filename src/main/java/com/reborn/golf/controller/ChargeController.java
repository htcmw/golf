package com.reborn.golf.controller;


import com.reborn.golf.dto.kakaopay.KakaoPayRequestDto;
import com.reborn.golf.dto.kakaopay.KakaoPayResponseDto;
import com.reborn.golf.service.ChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
    public void readyPayment(@RequestBody KakaoPayRequestDto kakaoPayRequestDto){
        log.info(chargeService.readyPayment(kakaoPayRequestDto));
    }

    @PostMapping("/kakaopay/approval")
    public KakaoPayResponseDto kakaoPaymentApprove(@RequestParam("pg_token") String pgToken){
        return null;
    }
}
