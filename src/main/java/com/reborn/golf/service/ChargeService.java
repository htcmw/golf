package com.reborn.golf.service;

import com.reborn.golf.dto.kakaopay.*;
import com.reborn.golf.entity.Orders;
import org.springframework.web.client.RestClientException;

import java.net.URISyntaxException;

public interface ChargeService {
    KakaoPayResponseDto readyPayment(String partnerOrderId, String partnerUserId, String itemName, Integer quantity, Integer totalAmount, Integer taxFreeAmount) throws URISyntaxException;

    KakaoPayApprovalDto approvePayment(String orderId, String pgToken);

    KakaoPayCancelResponseDto cancelPayment(String orderId) throws RestClientException, URISyntaxException;

    void cancelPayment(Orders orders) throws RestClientException, URISyntaxException;
}
