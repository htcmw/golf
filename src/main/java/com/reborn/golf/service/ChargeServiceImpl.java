package com.reborn.golf.service;

import com.reborn.golf.dto.kakaopay.KakaoPayApprovalDto;
import com.reborn.golf.dto.kakaopay.KakaoPayRequestDto;
import com.reborn.golf.dto.kakaopay.KakaoPayResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Log4j2
public class ChargeServiceImpl implements ChargeService{

    @Value("${KAKAOPAY-HOST}")
    private String HOST;

    private KakaoPayResponseDto kakaoPayResponseDto;

    @Override
    public String readyPayment(KakaoPayRequestDto kakaoPayRequestDto) {

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " +"d9b58db600786919de1739863953c201");
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        System.out.println("partnerUserId : " + kakaoPayRequestDto.getPartnerUserId());

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", kakaoPayRequestDto.getPartnerOrderId());
        params.add("partner_user_id", kakaoPayRequestDto.getPartnerUserId());
        params.add("item_name", kakaoPayRequestDto.getTitle());
        params.add("quantity", "1");
        params.add("total_amount", kakaoPayRequestDto.getTotalAmount().toString());
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:8089/payments/kakaopay/approval");
        params.add("cancel_url", "http://localhost:8089/payments/kakaopay/cancel");
        params.add("fail_url", "http://localhost:8089/payments/kakaopay/fail");

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            kakaoPayResponseDto = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayResponseDto.class);

            log.info("" + kakaoPayResponseDto);

            return kakaoPayResponseDto.getNext_redirect_pc_url();

        } catch (RestClientException | URISyntaxException e) {
            log.info(e.getMessage());
        }

        return null;
    }

    @Override
    public KakaoPayApprovalDto approvePayment(String pgToken) {

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "d9b58db600786919de1739863953c201");
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayResponseDto.getTid());
        params.add("partner_order_id", kakaoPayResponseDto.getPartnerOrderId());
        params.add("partner_user_id", kakaoPayResponseDto.getPartnerUserId());
        params.add("pg_token", pgToken);
        params.add("total_amount", kakaoPayResponseDto.getTotalAmount().toString());

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            KakaoPayApprovalDto kakaoPayApprovalDto = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalDto.class);
            log.info(kakaoPayApprovalDto);
            return kakaoPayApprovalDto;

        } catch (RestClientException | URISyntaxException e) {
            log.info(e.getMessage());
        }
        return null;
    }
}
