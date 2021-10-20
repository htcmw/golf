package com.reborn.golf.service;

import com.reborn.golf.api.CoinExchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UpbitTests {

    @Autowired
    private CoinExchange coinExchange;
    @Test
    public void getPrice(){
        System.out.println(coinExchange.getTokenPrice());
    }
}
