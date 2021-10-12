package com.reborn.golf.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class test {

    @Test
    public void abc(){
        String orderId = LocalDate.now() + UUID.randomUUID().toString();

        System.out.println(orderId);
    }
}
