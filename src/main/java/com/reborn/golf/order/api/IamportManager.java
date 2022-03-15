package com.reborn.golf.order.api;


import com.reborn.golf.order.dto.OrdersDto;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@Log4j2
public class IamportManager {

    private final IamportClient iamportClient;

    public IamportManager() {
        this.iamportClient = new IamportClient("3870373320666877", "f605a3631b647fcd153d134aebfc442eb638d7daea68be45cda866a367c41589cf45f625d63da0b5");
    }

    public boolean verificatePayment(OrdersDto ordersDto) throws IamportResponseException, IOException {
        IamportResponse<Payment> info = iamportClient.paymentByImpUid(ordersDto.getImpUid());

        if (ordersDto.getImpUid().equals(info.getResponse().getImpUid())
                && ordersDto.getOrderNumber().equals(info.getResponse().getMerchantUid())
                && ordersDto.getOrderName().equals(info.getResponse().getName())
                && ordersDto.getUserEmail().equals(info.getResponse().getBuyerEmail())
                && ordersDto.getUserName().equals(info.getResponse().getBuyerName())
                && ordersDto.getRecipientPhone().equals(info.getResponse().getBuyerTel())
                && ordersDto.getRecipientAddress().equals(info.getResponse().getBuyerAddr())
                && ordersDto.getTotalPrice() == info.getResponse().getAmount().intValue())
            return true;
        return false;
    }


    public void cancelPayment(String impUid, Integer amount, boolean isPartialCancel, String reason) throws IamportResponseException, IOException {
        CancelData cancelData = null;
        if (isPartialCancel)
            cancelData = new CancelData(impUid, true, BigDecimal.valueOf(amount)); //imp_uid를 통한 500원 부분취소
        else
            cancelData = new CancelData(impUid, true); //imp_uid를 통한 500원 부분취소
        cancelData.setReason(reason);

        IamportResponse<Payment> payment_response = iamportClient.cancelPaymentByImpUid(cancelData);

        // 이미 취소된 거래는 null
        if (payment_response.getResponse() == null) {
            log.info("already canceled");
        }
        else{
            log.info("now canceled");
            log.info(payment_response.getResponse().getCancelAmount());
            log.info(payment_response.getResponse().getCancelReason());
        }
    }

    public boolean verification(String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> info = iamportClient.paymentByImpUid(impUid);
        OrdersDto ordersDto;
        log.info("==============================================");
        log.info("getImpUid : " + info.getResponse().getImpUid());
        log.info("getMerchantUid : " + info.getResponse().getMerchantUid());
        log.info("getBuyerEmail : " + info.getResponse().getBuyerEmail());
        log.info("getBuyerName : " + info.getResponse().getBuyerName());
        log.info("getName : " + info.getResponse().getName());
        log.info("getPaidAt : " + info.getResponse().getPaidAt());
        log.info("getPayMethod : " + info.getResponse().getPayMethod());
        log.info("getBuyerAddr : " + info.getResponse().getBuyerAddr());
        log.info("getAmount : " + info.getResponse().getAmount());
        log.info("getBuyerTel : " + info.getResponse().getBuyerTel());
        log.info("getBuyerPostcode : " + info.getResponse().getBuyerPostcode());
        log.info("getStatus : " + info.getResponse().getStatus());
        log.info("getPgTid : " + info.getResponse().getPgTid());
        log.info("getPgProvider : " + info.getResponse().getPgProvider());
        log.info("getReceiptUrl : " + info.getResponse().getReceiptUrl());
        log.info("==============================================");
        return true;
    }


}
