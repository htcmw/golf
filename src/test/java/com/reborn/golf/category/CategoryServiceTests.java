package com.reborn.golf.category;

import com.reborn.golf.category.dto.CategoryDto;
import com.reborn.golf.category.entity.Category;
import com.reborn.golf.category.repository.CategoryRepository;
import com.reborn.golf.category.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class CategoryServiceTests {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void getListTest() {
        List<CategoryDto> categoryDtos = categoryService.getList();
        showDto(categoryDtos, 0);
    }

    private void showDto(List<CategoryDto> categorys, int depth) {
        if (categorys == null)
            return;

        for (CategoryDto categoryDto : categorys) {
            for (int i = 0; i < depth; i++) {
                System.out.print("      ");
            }
            System.out.println(categoryDto);
            showDto(categoryDto.getCategories(), depth + 1);
        }
    }


    @Test
    public void registerTest() {
        CategoryDto categoryDto = CategoryDto.builder()
                .name("Category " + "builder확인")
                .pidx(25)
                .priority(11)
                .build();
        categoryService.register(categoryDto);
    }


    @Test
    public void modifyTest() {
        Category category1 = categoryRepository.getByIdxAndAndRemovedFalse(10);
        CategoryDto categoryDto = CategoryDto.builder()
                .idx(10)
                .name("Category 수정 3번째")
                .pidx(4)
                .priority(2)
                .build();
        categoryService.modify(categoryDto);
        Category category2 = categoryRepository.getByIdxAndAndRemovedFalse(10);
        System.out.println(category1);
        System.out.println(category2);
    }

    @Test
    public void removed() {
        categoryService.remove(1);
    }
}
