package com.reborn.golf.service;

import com.reborn.golf.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceTests {
    @Autowired
    ProductService productService;
    @Test
    public void getListTest(){
//        System.out.println(productService.getList(new PageRequestDto()));
    }
}
