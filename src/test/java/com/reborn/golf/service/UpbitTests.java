package com.reborn.golf.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class UpbitTests {

    @Test
    public void getPrice(){
        WebClient webClient = WebClient.create();
    }
}
