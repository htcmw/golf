package com.reborn.golf.service;

import com.reborn.golf.dto.kakaopay.*;
import com.reborn.golf.entity.Enum.DeliveryStatus;
import com.reborn.golf.entity.Enum.OrderStatus;
import com.reborn.golf.entity.Orders;
import com.reborn.golf.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChargeServiceImpl implements ChargeService {


    @Value("${KAKAOPAY_CID}")
    private String CID;

    @Value("${KAKAOPAY_HOST}")
    private String HOST;

    @Value("${KAKAOPAY_HOST_KEY}")
    private String HOST_KEY;

    private final OrderRepository orderRepository;

    @Override
    public KakaoPayResponseDto readyPayment(String partnerOrderId, String partnerUserId, String itemName, Integer quantity, Integer totalAmount, Integer taxFreeAmount) throws RestClientException, URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = makeHeaders();
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", CID);
        params.add("partner_order_id", partnerOrderId);
        params.add("partner_user_id", partnerUserId);
        params.add("item_name", itemName);
        params.add("quantity", quantity.toString());
        params.add("total_amount", totalAmount.toString());
        params.add("tax_free_amount", taxFreeAmount.toString());
        params.add("approval_url", "http://localhost:8089/payments/kakaopay/approval/" + partnerOrderId);
        params.add("cancel_url", "http://localhost:8089/payments/kakaopay/cancel" + partnerUserId);
        params.add("fail_url", "http://localhost:8089/payments/kakaopay/fail");

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        KakaoPayResponseDto kakaoPayResponseDto = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayResponseDto.class);

        log.info(kakaoPayResponseDto);

        return kakaoPayResponseDto;

    }

    private HttpHeaders makeHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + HOST_KEY);
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    @Override
    public KakaoPayApprovalDto approvePayment(String partnerOrderId, String pgToken) {
        Orders orders = orderRepository.getOrdersByPartnerOrderId(partnerOrderId);

        RestTemplate restTemplate = new RestTemplate();
        // 서버로 요청할 Header
        HttpHeaders headers = makeHeaders();
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", CID);
        params.add("tid", orders.getTid());
        params.add("partner_order_id", orders.getPartnerOrderId());
        params.add("partner_user_id", orders.getMember().getEmail());
        params.add("pg_token", pgToken);
        params.add("total_amount", orders.getTotalPrice().toString());

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            KakaoPayApprovalDto kakaoPayApprovalDto = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalDto.class);
            log.info(kakaoPayApprovalDto);
            orders.setChargeApprovedAt(kakaoPayApprovalDto.getApproved_at());
            orders.setChargeCreatedAt(kakaoPayApprovalDto.getCreated_at());
            orders.setOrderState(OrderStatus.PROCESSING);
            orders.getDelivery().setDeliveryStatus(DeliveryStatus.READY);

            orderRepository.save(orders);

            return kakaoPayApprovalDto;

        } catch (RestClientException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public KakaoPayCancelResponseDto cancelPayment(String partnerOrderId) throws RestClientException, URISyntaxException {
        Orders orders = orderRepository.getOrdersByPartnerOrderId(partnerOrderId);
        RestTemplate restTemplate = new RestTemplate();
        // 서버로 요청할 Header
        HttpHeaders headers = makeHeaders();
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", CID);
        params.add("tid", orders.getTid());
        params.add("cancel_amount", orders.getTotalPrice().toString());
        params.add("quantity", orders.getOrderProductsCount().toString());
        params.add("cancel_tax_free_amount", orders.getTaxFreeAmount().toString());

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        KakaoPayCancelResponseDto kakaoPayResponseDto = restTemplate.postForObject(new URI(HOST + "/v1/payment/cancel"), body, KakaoPayCancelResponseDto.class);


        return null;
    }
    @Override
    public void cancelPayment(Orders orders) throws RestClientException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        // 서버로 요청할 Header
        HttpHeaders headers = makeHeaders();
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", CID);
        params.add("tid", orders.getTid());
        params.add("cancel_amount", orders.getTotalPrice().toString());
        params.add("quantity", orders.getOrderProductsCount().toString());
        params.add("cancel_tax_free_amount", orders.getTaxFreeAmount().toString());

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        KakaoPayCancelResponseDto kakaoPayResponseDto = restTemplate.postForObject(new URI(HOST + "/v1/payment/cancel"), body, KakaoPayCancelResponseDto.class);
    }
}
