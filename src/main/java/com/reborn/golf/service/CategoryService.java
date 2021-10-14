package com.reborn.golf.service;

import com.reborn.golf.controller.CategoryDto;
import com.reborn.golf.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getList();
    Integer register(String categoryName);
    Integer modify(CategoryDto categoryDto);
    Integer remove(Integer categoryIdx);


    default CategoryDto entiryToDto(Category category){
        return CategoryDto.builder()
                .idx(category.getIdx())
                .name(category.getName())
                .code(category.getCode())
                .priority(category.getPriority())
                .build();
    }
    default Category dtoToEntiry(CategoryDto categoryDto){
        return Category.builder()
                .idx(categoryDto.getIdx())
                .name(categoryDto.getName())
                .build();
    }
}
