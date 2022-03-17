package com.reborn.golf.product;

import com.reborn.golf.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class ProductTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void getListTest() {
        Page<Object[]> result = productRepository.getProductList("02", PageRequest.of(0, 10, Sort.by("salesVolume").descending()));
    }
}
