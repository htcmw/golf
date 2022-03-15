package com.reborn.golf.repository;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.product.entity.Product;
import com.reborn.golf.product.entity.ProductImage;
import com.reborn.golf.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ProductRepositoryTests {
    @Autowired
    ProductRepository productRepositoryTests;

    @Test
    @Transactional
    @Rollback(value = false)
    public void getProductList() {
        PageRequestDto pageRequestDto = new PageRequestDto();
        Page<Object[]> result = productRepositoryTests.getProductList("0205", pageRequestDto.getPageable(Sort.by("salesVolume").descending()));
        result.forEach(arr -> {
            System.out.println((Product) arr[0]);
            System.out.println((ProductImage) arr[1]);
            System.out.println((Double) arr[2]);
            System.out.println((Long) arr[3]);
        });
    }
}
