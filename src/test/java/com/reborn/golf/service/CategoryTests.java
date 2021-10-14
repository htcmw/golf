package com.reborn.golf.service;

import com.reborn.golf.entity.Category;
import com.reborn.golf.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class CategoryTests {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void firstCategory(){
        String[] arr = {"골프클럽", "골프용품", "골프웨어"};
        int cnt = 0;
        for(String temp : arr){
            cnt = cnt + 1;
            categoryRepository.save(Category.builder().name(temp).priority(cnt).code(String.format("%02d",cnt)).build());
        }
    }
    @Test
    public void secondCategory(){
        String[][] arr = {{"드라이버","우드","아이언","풀세트"},{"골프공","골프가방","골프장갑","골프모자","골프화"},{"아우터","상의","하의","양말"}};
        int cnt = 0;
        for(int i = 0 ; i < arr.length ; i++){
            Category category = categoryRepository.getById(i+1);
            for(String temp : arr[i]){
                cnt = cnt + 1;
                Category category1 = Category.builder().code(category.getCode() + String.format("%02d",cnt)).name(temp).priority(cnt).build();
                category1.setParent(category);
                categoryRepository.save(category1);
            }

        }

    }
    @Test
    public void findAllWithQuerydslTest(){
        List<Category> categoryList = categoryRepository.getCategories();
    }
}
