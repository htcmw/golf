package com.reborn.golf.service;

import com.reborn.golf.order.api.IamportManager;
import com.siot.IamportRestClient.exception.IamportResponseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class IamportManagerTests {
    @Autowired
    IamportManager iamportManager;
    @Test
    public void cancelPaymentTest(){
        try {
            iamportManager.cancelPayment("imp_278838963242",null,false,"테스트");
        } catch (IamportResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
