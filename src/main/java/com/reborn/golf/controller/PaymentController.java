package com.reborn.golf.controller;


import com.reborn.golf.dto.KakaoPayResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/payments")
@Log4j2
@RequiredArgsConstructor
public class PaymentController {

    @Value("${KAKAOPAY-HOST}")
    private String host;

    @PostMapping("/kakaopay/ready")
    public KakaoPayResponseDto kakaoPaymentReady(@RequestParam String partnerOrderId,
                                                 @RequestParam String partnerUserId,
                                                 @RequestParam String title,
                                                 @RequestParam Integer totalAmount){


        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " +"d9b58db600786919de1739863953c201");
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        System.out.println("partnerUserId : " + partnerUserId);


        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", partnerOrderId);
        params.add("partner_user_id", partnerUserId);
        params.add("item_name", title);
        params.add("quantity", "1");
        params.add("total_amount", totalAmount.toString());
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:8089/payments/kakaopay/approve");
        params.add("cancel_url", "http://localhost:8089/cancel");
        params.add("fail_url", "http://localhost:8089/fail");

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        System.out.println(host);
        try {
            KakaoPayResponseDto kakaoPayResponseDto = restTemplate.postForObject(new URI(host + "/v1/payment/ready"), body, KakaoPayResponseDto.class);

            log.info("" + kakaoPayResponseDto);

            return kakaoPayResponseDto;

        } catch (RestClientException | URISyntaxException e) {

            log.info(e.getMessage());
        }

        return null;
    }


    @PostMapping("/kakaopay/approve")
    public KakaoPayResponseDto kakaoPaymentApprove(@RequestParam("partner_order_id") String partnerOrderId,
                                                 @RequestParam("partner_user_id") String partnerUserId,
                                                 @RequestParam("item_name") String title,
                                                 @RequestParam("total_amount") Integer totalAmount){


        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " +"d9b58db600786919de1739863953c201");
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        System.out.println("partnerUserId : " + partnerUserId);


        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", partnerOrderId);
        params.add("partner_user_id", partnerUserId);
        params.add("item_name", title);
        params.add("quantity", "1");
        params.add("total_amount", totalAmount.toString());
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:8089/success");
        params.add("cancel_url", "http://localhost:8089/cancel");
        params.add("fail_url", "http://localhost:8089/fail");

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        System.out.println(host);
        try {
            KakaoPayResponseDto kakaoPayResponseDto = restTemplate.postForObject(new URI(host + "/v1/payment/ready"), body, KakaoPayResponseDto.class);

            log.info("" + kakaoPayResponseDto);

            return kakaoPayResponseDto;

        } catch (RestClientException | URISyntaxException e) {

            log.info(e.getMessage());
        }

        return null;
    }
}
