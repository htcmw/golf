package com.reborn.golf.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryServiceTests {
    @Autowired
    private CategoryService categoryService;
    @Test
    public void getList(){
        System.out.println(categoryService.getList());

    }

}
